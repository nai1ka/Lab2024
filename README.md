# Tinkoff Lab 2024

## Кратко о реализованных фичах и технологиях:
* Список фильмов - RecyclerView ([MovieListAdapter.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fadapters%2FMovieListAdapter.kt))
* Получение данных с сервера - Retrofit ([WebRepository.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fdata%2FWebRepository.kt))
* Сохранение избранных фильмов в базу данных SQLite - Room ([LocalRepository.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fdata%2FLocalRepository.kt))
* MVVM архитектура ([MovieListViewModel.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fui%2Fpopular%2FMovieListViewModel.kt), [FavouriteViewModel.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fui%2Ffavourite%2FFavouriteViewModel.kt))
* Кэширование ответов от API в [CacheManager.kt](app%2Fsrc%2Fmain%2Fjava%2Fru%2Fndevelop%2Ftinkofflab2024%2Fdata%2FCacheManager.kt)
* Отдельная разметка для альбомного режима ([activity_main.xml](app%2Fsrc%2Fmain%2Fres%2Flayout-land%2Factivity_main.xml))
* Поиск по фильмам в обох разделах 
* Тёмная тема
* Корутины
* Использованы шиммеры и индикаторы загрузки
* Обработка ошибок при загрузке


### Видео: [drive.google.com](https://drive.google.com/file/d/125fJB2diIYMTLyFGuJvEFgr2mXWx036I/view?usp=share_link)

