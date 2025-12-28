package com.example.kinotlin.titles.presentation

import com.example.kinotlin.titles.presentation.model.TitleType
import com.example.kinotlin.titles.presentation.model.TitleUiModel

object MockData {
    fun getTitles(): List<TitleUiModel> = listOf(
        TitleUiModel(
            id = "tt13207736",
            type = TitleType.TV_SERIES,
            primaryTitle = "Monster",
            originalTitle = "Monster",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BZGY2OTcxYWItZDgxNi00Y2E1LTk0YTgtZDcwZjZkNzU4OTJjXkEyXkFqcGc@._V1_.jpg",
                width = 1500,
                height = 2222
            ),
            startYear = 2022,
            runtimeSeconds = 3600,
            genres = listOf("Biography", "Crime", "Drama", "Thriller"),
            rating = TitleUiModel.Rating(
                aggregateRating = 7.8,
                voteCount = 199853
            ),
            plot = "An anthology series about high-profile crimes or killers that captured public attention and notoriety."
        ),
        TitleUiModel(
            id = "tt30144839",
            type = TitleType.MOVIE,
            primaryTitle = "One Battle After Another",
            originalTitle = "One Battle After Another",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BMzBkZmQ0NjMtNTZlMy00ZjdlLTg5ODUtYWFlNGM0YzE3MTg0XkEyXkFqcGc@._V1_.jpg",
                width = 1638,
                height = 2048
            ),
            startYear = 2025,
            runtimeSeconds = 9660,
            genres = listOf("Action", "Crime", "Drama", "Thriller"),
            rating = TitleUiModel.Rating(
                aggregateRating = 8.3,
                voteCount = 76075
            ),
            plot = "When their evil enemy resurfaces after 16 years, a group of ex-revolutionaries reunite to rescue one of their own's daughter."
        ),
        TitleUiModel(
            id = "tt27427326",
            type = TitleType.TV_MINI_SERIES,
            primaryTitle = "Wayward",
            originalTitle = "Wayward",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BZTM0NTQ5ZTktNzIwZS00OWRhLWIxZGEtNTUyMzkzZDVhOTFmXkEyXkFqcGc@._V1_.jpg",
                width = 1500,
                height = 2222
            ),
            startYear = 2025,
            endYear = 2025,
            runtimeSeconds = 2700,
            genres = listOf("Drama", "Mystery", "Thriller"),
            rating = TitleUiModel.Rating(
                aggregateRating = 5.9,
                voteCount = 13019
            ),
            plot = "A bucolic but sinister town explores the insidious intricacies of the troubled teen industry, and the eternal struggle of the next generation."
        ),
        TitleUiModel(
            id = "tt13542714",
            type = TitleType.TV_SERIES,
            primaryTitle = "House of Guinness",
            originalTitle = "House of Guinness",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BM2IzZDg0ODYtYWUyZi00YmJmLWE2N2MtMmRmNjAzMWRiYTAzXkEyXkFqcGc@._V1_.jpg",
                width = 1080,
                height = 1350
            ),
            startYear = 2025,
            runtimeSeconds = 3000,
            genres = listOf("Biography", "Drama", "History"),
            rating = TitleUiModel.Rating(
                aggregateRating = 7.5,
                voteCount = 9975
            ),
            plot = "Follows the aftermath of the death of brewery mogul, Sir Benjamin Guinness, and the great impact of his will on the fate of his four adult children."
        ),
        TitleUiModel(
            id = "tt21103218",
            type = TitleType.MOVIE,
            primaryTitle = "The Lost Bus",
            originalTitle = "The Lost Bus",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BZTIzNmQzYzUtNTdlNi00NmY5LThmNTYtMGFmZjUxMTgzOGNmXkEyXkFqcGc@._V1_.jpg",
                width = 2000,
                height = 3000
            ),
            startYear = 2025,
            runtimeSeconds = 7740,
            genres = listOf("Biography", "Drama", "History", "Thriller"),
            rating = TitleUiModel.Rating(
                aggregateRating = 7.0,
                voteCount = 10770
            ),
            plot = "A wayward school bus driver and a dedicated school teacher battle to save 22 children from a terrifying inferno."
        ),
        TitleUiModel(
            id = "tt18392014",
            type = TitleType.MOVIE,
            primaryTitle = "Play Dirty",
            originalTitle = "Play Dirty",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BN2E4MmM2ODctYTIyNS00ZGIxLWJmYjctYTUxYThhNzY4MmMzXkEyXkFqcGc@._V1_.jpg",
                width = 1080,
                height = 1920
            ),
            startYear = 2025,
            runtimeSeconds = 7500,
            genres = listOf("Action", "Crime", "Drama", "Thriller"),
            rating = TitleUiModel.Rating(
                aggregateRating = 5.9,
                voteCount = 8393
            ),
            plot = "A ruthless thief and his expert crew stumble onto the heist of a lifetime."
        ),
        TitleUiModel(
            id = "tt13146488",
            type = TitleType.TV_SERIES,
            primaryTitle = "Peacemaker",
            originalTitle = "Peacemaker",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BZDIyMGU2NTktOTM3YS00OTRjLWJiMmItNDEyNjVhZTZiZGUxXkEyXkFqcGc@._V1_.jpg",
                width = 1296,
                height = 1920
            ),
            startYear = 2022,
            runtimeSeconds = 2400,
            genres = listOf("Action", "Adventure", "Comedy", "Crime", "Drama", "Fantasy", "Sci-Fi"),
            rating = TitleUiModel.Rating(
                aggregateRating = 8.3,
                voteCount = 171716
            ),
            plot = "Picking up where The Suicide Squad (2021) left off, Peacemaker returns home after recovering from his encounter with Bloodsport."
        ),
        TitleUiModel(
            id = "tt32985279",
            type = TitleType.MOVIE,
            primaryTitle = "Steve",
            originalTitle = "Steve",
            primaryImage = TitleUiModel.Image(
                url = "https://m.media-amazon.com/images/M/MV5BYmE4N2ZlNWQtMDRhNC00ZmYzLWI5ODMtODAzZjRiOTkxZGZhXkEyXkFqcGc@._V1_.jpg",
                width = 1500,
                height = 2222
            ),
            startYear = 2025,
            runtimeSeconds = 5580,
            genres = listOf("Comedy", "Drama"),
            rating = TitleUiModel.Rating(
                aggregateRating = 6.6,
                voteCount = 4644
            ),
            plot = "Follows headteacher Steve battling for his reform college's survival while managing his mental health."
        )
    )
}