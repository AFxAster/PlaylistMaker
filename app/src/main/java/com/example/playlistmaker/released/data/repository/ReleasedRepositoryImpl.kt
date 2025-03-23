package com.example.playlistmaker.released.data.repository

import com.example.playlistmaker.released.domain.entity.Release
import com.example.playlistmaker.released.domain.repository.ReleasedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class ReleasedRepositoryImpl : ReleasedRepository {
    override fun getReleasedFrom(date: Date): Flow<List<Release>> = flow {
        emit(
            listOf(
                Release("1", "Сложная", "Лилая", "сингл", "", Date()),
                Release("2", "Дико, например", "Pharaoh", "сингл", "", Date()),
                Release("3", "Прометей роняет факел", "Horus", "альбом", "", Date()),
                Release("4", "Не хватит сил", "слёзы в ампулах", "сингл", "", Date()),
                Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
                Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
                Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
            )
        )
    }
}
