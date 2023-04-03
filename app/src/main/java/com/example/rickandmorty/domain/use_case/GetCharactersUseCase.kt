package com.example.rickandmorty.domain.use_case

import com.example.rickandmorty.data.Result
import com.example.rickandmorty.domain.model.Characters
import com.example.rickandmorty.domain.repositories.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int, query: String? = null): Flow<Result<List<Characters>>> {
        return if (query.isNullOrBlank()) {
            repository.getCharacters(page)
        } else {
            repository.searchCharacters(page, query)
        }
    }
}
