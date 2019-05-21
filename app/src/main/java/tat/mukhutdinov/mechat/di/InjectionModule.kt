package tat.mukhutdinov.mechat.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tat.mukhutdinov.mechat.repo.MessagesBoundaryRepo
import tat.mukhutdinov.mechat.repo.MessagesRepo
import tat.mukhutdinov.mechat.ui.MainViewModel
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