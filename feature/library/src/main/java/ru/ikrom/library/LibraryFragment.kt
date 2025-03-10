package ru.ikrom.library

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.ikrom.base_fragment.DefaultListFragment
import ru.ikrom.theme.AppDrawableIds
import ru.ikrom.base_adapter.CompositeAdapter
import ru.ikrom.adapter_delegates.delegates.MenuNavigateDelegate
import ru.ikrom.adapter_delegates.delegates.MenuNavigateItem
import ru.ikrom.adapter_delegates.delegates.NestedItems
import ru.ikrom.adapter_delegates.delegates.NestedItemsDelegate
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumAdapter
import ru.ikrom.adapter_delegates.delegates.TitleDelegate
import ru.ikrom.adapter_delegates.delegates.TitleItem
import ru.ikrom.library.dialogs.AudioQualityDialog
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : DefaultListFragment<UiState, LibraryViewModel>(R.layout.fragment_library){
    @Inject
    lateinit var navigator: Navigator
    override val mViewModel: LibraryViewModel by viewModels()

    private var audioQuality = AudioQualityDialog.AudioQuality.AUTO
    private val compositeAdapter = CompositeAdapter.Builder()
        .add(TitleDelegate())
        .add(MenuNavigateDelegate())
        .add(NestedItemsDelegate())
        .build()

    private val screens by lazy {
        listOf(
            TitleItem(getString(R.string.title_library)),
            MenuNavigateItem(AppDrawableIds.FAVORITE, getString(R.string.menu_liked_tracks), { navigator.toFavoriteTracks() }),
            MenuNavigateItem(AppDrawableIds.ALBUM, getString(R.string.menu_liked_albums), { navigator.toFavoriteAlbums() }),
            MenuNavigateItem(AppDrawableIds.ARTIST, getString(R.string.menu_liked_artists), { navigator.toFavoriteArtists() }),
            MenuNavigateItem(AppDrawableIds.PLAYLIST, getString(R.string.menu_playlist), { navigator.toPlaylists() })
        )
    }

    private val options by lazy {
        listOf(
            TitleItem("Управление"),
            MenuNavigateItem(AppDrawableIds.STORAGE, "Удалить историю прослушивания", { resetItems() }, false),
            MenuNavigateItem(AppDrawableIds.AUDIO_QUALITY, "Выбор качества аудио", { showQualityDialog(audioQuality) }, false),
        )
    }

    override fun getAdapter(): RecyclerView.Adapter<*> = compositeAdapter
    override fun getRecyclerViewId() = R.id.rv_main
    override fun getLayoutManager() = LinearLayoutManager(context)
    override fun handleState(state: UiState) {
        compositeAdapter.setItems(screens)
        when(state){
            is UiState.Success -> onSuccessState(state)
            else -> {}
        }
        compositeAdapter.addAll(options)
    }

    private fun onSuccessState(data: UiState.Success){
        if(data.data.isNotEmpty()){
            compositeAdapter.add(TitleItem("В последний раз слушали"))
            compositeAdapter.add(NestedItems(
                items = data.data,
                adapter = CompositeAdapter.Builder()
                    .add(ThumbnailMediumAdapter(
                        onClick = {mViewModel.playTrack(it)},
                        onLongClick = { navigator.toTrackMenu(it) }
                )).build()
            ))
        }
    }

    private fun resetItems(){
        compositeAdapter.setItems(emptyList())
        compositeAdapter.addAll(screens)
        compositeAdapter.addAll(options)
    }

    fun showQualityDialog(currentQuality: AudioQualityDialog.AudioQuality) {
        val dialog = AudioQualityDialog().apply {
            setParams(currentQuality, object : AudioQualityDialog.QualitySelectionListener {
                override fun onQualitySelected(quality: AudioQualityDialog.AudioQuality) {
                    audioQuality = quality
//                    viewModel.setAudioQuality(quality)
                }
            })
        }
        dialog.show(parentFragmentManager, "AudioQualityDialog")
    }

    interface Navigator{
        fun toTrackMenu(item: ThumbnailItem)
        fun toFavoriteTracks()
        fun toFavoriteAlbums()
        fun toPlaylists()
        fun toFavoriteArtists()
        fun toLocalTracks()
    }
}