package ru.ikrom.player_screen

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.Player
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.player_screen.databinding.FragmentPlayerBinding
import ru.ikrom.theme.AppIconsId
import ru.ikrom.utils.ColorsUtil
import ru.ikrom.utils.setupMarginFromStatusBar

@AndroidEntryPoint
class PlayerFragment : Fragment(R.layout.fragment_player) {
    private val playerViewModel: PlayerViewModel by viewModels()
    private lateinit var binding: FragmentPlayerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)
        setupMarginFromStatusBar(binding.handle)
        setupContent()
        setupButtons()
        setupButtonsListener()
        setupSeekBar()
    }

    private fun setupButtons() {
        playerViewModel.isPlayingLiveData.observe(viewLifecycleOwner){
            binding.btnPlayPause.setImageResource(if (it) AppIconsId.pause else AppIconsId.play)
        }
        playerViewModel.repeatModeLiveData.observe(viewLifecycleOwner){repeatMode ->
            when(repeatMode) {
                Player.REPEAT_MODE_OFF -> binding.btnToRepeat.setImageResource(AppIconsId.repeatOff)
                Player.REPEAT_MODE_ONE -> binding.btnToRepeat.setImageResource(AppIconsId.repeatOne)
                Player.REPEAT_MODE_ALL -> binding.btnToRepeat.setImageResource(AppIconsId.repeatAll)
            }
        }
        playerViewModel.currentMediaItemLiveData.observe(viewLifecycleOwner){mediaItem ->
            if (mediaItem != null) {
                playerViewModel.isFavorite(mediaItem.mediaId).observe(viewLifecycleOwner) {
                    binding.btnToFavorite.setImageResource(if (it) AppIconsId.favorite else AppIconsId.favoriteBordered)
                }
            }
        }
    }

    private fun setupSeekBar(){
        binding.progressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playerViewModel.seekTo(binding.progressBar.progress.toLong())
            }
        })
        setSeekbarMaxValue()
        updateSeekBarPosition()
    }

    private fun setSeekbarMaxValue(){
        playerViewModel.totalDurationLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.max = it.toInt()
            binding.tvTotalTime.text = it.toTimeString()
        }
    }

    private fun updateSeekBarPosition(){
        playerViewModel.currentPositionLiveData.observe(viewLifecycleOwner){ position->
            binding.progressBar.progress = position.toInt()
            binding.tvProgressTime.text = position.toTimeString()
        }
    }

    private fun setupButtonsListener() {
        binding.btnPlayPause.setOnClickListener {
            playerViewModel.togglePlayPause()
        }
        binding.btnSkipNext.setOnClickListener {
            playerViewModel.seekToNext()
        }
        binding.btnSkipPrevious.setOnClickListener {
            playerViewModel.seekToPrevious()
        }
        binding.btnToRepeat.setOnClickListener {
            playerViewModel.toggleRepeat()
        }
        binding.btnToFavorite.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        binding.btnCast.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
        binding.btnQueue.setOnClickListener {
            val bottomQueueFragment = PlayerQueueFragment()
            bottomQueueFragment.show(parentFragmentManager, bottomQueueFragment.tag)
        }
        binding.btnCaptions.setOnClickListener {
            // TODO: Not yet implemented
            Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBackground(resource: Drawable) {
        val bitmap = (resource as BitmapDrawable).bitmap

        Palette.from(bitmap).generate { palette ->
            val dominantColor = ColorsUtil.adjustColorToBackground(palette?.getDominantColor(Color.BLACK) ?: Color.BLACK, true)
            val mutedColor = ColorsUtil.adjustColorToBackground(palette?.getMutedColor( Color.WHITE) ?: Color.WHITE, true)
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(mutedColor, dominantColor)
            )
            binding.container.background = gradientDrawable
        }
    }

    private fun setupContent() {
        playerViewModel.currentMediaItemLiveData.observe(viewLifecycleOwner) { currentItem ->
            if (currentItem != null) {
                setupThumbnail(currentItem.mediaMetadata.artworkUri.toString())
                binding.tvTrackTitle.text = currentItem.mediaMetadata.title
                binding.tvTrackAuthor.text = currentItem.mediaMetadata.artist
            }
        }
    }

    private fun setupThumbnail(imageUri: String){
        Glide
            .with(requireContext())
            .load(imageUri)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(requireContext(), "Fail to load image", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    setupBackground(resource)
                    return false
                }

            })
            .into(binding.ivTrackCover)
    }
}