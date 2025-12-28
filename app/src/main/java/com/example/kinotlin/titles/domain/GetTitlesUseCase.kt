package com.example.kinotlin.titles.domain

class GetTitlesUseCase(
    private val repository: TitlesRepository,
) {
    suspend operator fun invoke(filter: TitlesFilter = TitlesFilter()) = repository.getTitles(filter)
}
