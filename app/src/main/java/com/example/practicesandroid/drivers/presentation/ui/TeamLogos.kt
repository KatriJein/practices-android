package com.example.practicesandroid.drivers.presentation.ui

object TeamLogos {
    val logos = mapOf(
        "mclaren" to "https://upload.wikimedia.org/wikipedia/en/thumb/6/66/McLaren_Racing_logo.svg/250px-McLaren_Racing_logo.svg.png",
        "mercedes" to "https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Mercedes-Benz_in_Formula_One_logo.svg/250px-Mercedes-Benz_in_Formula_One_logo.svg.png",
        "ferrari" to "https://upload.wikimedia.org/wikipedia/en/thumb/d/df/Scuderia_Ferrari_HP_logo_24.svg/250px-Scuderia_Ferrari_HP_logo_24.svg.png",
        "red_bull" to "https://upload.wikimedia.org/wikipedia/en/4/44/Red_bull_racing.png",
        "williams" to "https://upload.wikimedia.org/wikipedia/en/thumb/d/d9/Atlassian_Williams_Racing_2025.svg/250px-Atlassian_Williams_Racing_2025.svg.png",
        "rb" to "https://upload.wikimedia.org/wikipedia/en/thumb/2/2b/VCARB_F1_logo.svg/250px-VCARB_F1_logo.svg.png",
        "aston_martin" to "https://upload.wikimedia.org/wikipedia/en/thumb/1/15/Aston_Martin_Aramco_2024_logo.png/250px-Aston_Martin_Aramco_2024_logo.png",
        "sauber" to "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/2023_Stake_F1_Team_Kick_Sauber_logo.png/250px-2023_Stake_F1_Team_Kick_Sauber_logo.png",
        "haas" to "https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/MoneyGram_Haas_F1_Team_Logo.svg/250px-MoneyGram_Haas_F1_Team_Logo.svg.png",
        "alpine" to "https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Logo_of_alpine_f1_team_2022.png/250px-Logo_of_alpine_f1_team_2022.png"
    )

    fun getLogo(teamId: String?): String? = logos[teamId]
}
