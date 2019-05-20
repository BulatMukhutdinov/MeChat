package tat.mukhutdinov.mechat.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.mechat.repo.MessagesBoundaryRepo
import tat.mukhutdinov.mechat.repo.MessagesRepo
import tat.mukhutdinov.mechat.ui.MainViewModel

object InjectionModule {

    val module = module {

        viewModel {
            MainViewModel(get())
        }

        single<MessagesRepo> {
            MessagesBoundaryRepo()
        }
    }
}