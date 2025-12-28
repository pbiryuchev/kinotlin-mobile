package com.example.kinotlin.titles.domain

class GetTitleDetailsUseCase(
    private val repository: TitlesRepository,
) {
    suspend operator fun invoke(titleId: String) = repository.getTitleById(titleId)
}
