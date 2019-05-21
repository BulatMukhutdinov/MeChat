package tat.mukhutdinov.mechat.main.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.mechat.main.repo.MessagesBoundaryRepo
import tat.mukhutdinov.mechat.main.repo.MessagesRepo
import tat.mukhutdinov.mechat.main.ui.MainViewModel
import java.lang.RuntimeException

object InjectionModule {

    val module = module {

        viewModel {
            MainViewModel(get())
        }

        single<MessagesRepo> {
            MessagesBoundaryRepo(FirebaseAuth.getInstance().currentUser ?: throw RuntimeException("Failed to identify user"))
        }
    }
}