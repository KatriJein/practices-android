package com.example.practicesandroid.drivers.presentation

import com.example.practicesandroid.drivers.presentation.model.Driver
import com.example.practicesandroid.drivers.presentation.model.Team
import java.time.LocalDate

object MockData {
    private val teams = listOf(
        Team(
            "mclaren", "McLaren Formula 1 Team", "Great Britain", 1966
        ),
        Team(
            "mercedes", "Mercedes Formula 1 Team", "Germany", 1954
        ),
        Team(
            "ferrari", "Scuderia Ferrari", "Italy", 1950
        ),
        Team(
            "red_bull", "Red Bull Racing", "Austria", 2005
        ),
        Team(
            "williams", "Williams Racing", "Great Britain", 1977
        ),
        Team(
            "rb", "RB F1 Team", "Italy", 2024
        ),
        Team(
            "aston_martin", "Aston Martin F1 Team", "Great Britain", 1959
        ),
        Team(
            "sauber", "Sauber F1 Team", "Switzerland", 1993
        ),
        Team(
            "haas", "Haas F1 Team", "United States", 2016
        ),
        Team(
            "alpine", "Alpine F1 Team", "France", 2021
        )
    )

    private val drivers = listOf(
        Driver(
            "piastri",
            "Oscar",
            "Piastri",
            "Australia",
            LocalDate.parse("2001-04-06"),
            81,
            team("mclaren"),
            324,
            1,
            7
        ),
        Driver(
            "norris",
            "Lando",
            "Norris",
            "Great Britain",
            LocalDate.parse("1999-11-13"),
            4,
            team("mclaren"),
            299,
            2,
            5
        ),
        Driver(
            "max_verstappen",
            "Max",
            "Verstappen",
            "Netherlands",
            LocalDate.parse("1997-09-30"),
            1,
            team("red_bull"),
            255,
            3,
            4
        ),
        Driver(
            "russell",
            "George",
            "Russell",
            "Great Britain",
            LocalDate.parse("1998-02-15"),
            63,
            team("mercedes"),
            212,
            4,
            1
        ),
        Driver(
            "leclerc",
            "Charles",
            "Leclerc",
            "Monaco",
            LocalDate.parse("1997-10-16"),
            16,
            team("ferrari"),
            165,
            5,
            0
        ),
        Driver(
            "hamilton",
            "Lewis",
            "Hamilton",
            "Great Britain",
            LocalDate.parse("1985-01-07"),
            44,
            team("ferrari"),
            121,
            6,
            0
        ),
        Driver(
            "antonelli",
            "Andrea",
            "Kimi Antonelli",
            "Italy",
            LocalDate.parse("2006-08-25"),
            12,
            team("mercedes"),
            78,
            7,
            0
        ),
        Driver(
            "albon",
            "Alex",
            "Albon",
            "Thailand",
            LocalDate.parse("1996-03-23"),
            23,
            team("williams"),
            70,
            8,
            0
        ),
        Driver(
            "hadjar",
            "Isack",
            "Hadjar",
            "France",
            LocalDate.parse("2004-02-28"),
            6,
            team("rb"),
            39,
            9,
            0
        ),
        Driver(
            "hulkenberg",
            "Nico",
            "Hulkenberg",
            "Germany",
            LocalDate.parse("1987-08-19"),
            27,
            team("sauber"),
            37,
            10,
            0
        ),
        Driver(
            "stroll",
            "Lance",
            "Stroll",
            "Canada",
            LocalDate.parse("1998-10-29"),
            18,
            team("aston_martin"),
            32,
            11,
            0
        ),
        Driver(
            "sainz",
            "Carlos",
            "Sainz",
            "Spain",
            LocalDate.parse("1994-09-01"),
            55,
            team("williams"),
            31,
            12,
            0
        ),
        Driver(
            "lawson",
            "Liam",
            "Lawson",
            "New Zealand",
            LocalDate.parse("2002-02-11"),
            30,
            team("rb"),
            30,
            13,
            0
        ),
        Driver(
            "alonso",
            "Fernando",
            "Alonso",
            "Spain",
            LocalDate.parse("1981-07-29"),
            14,
            team("aston_martin"),
            30,
            14,
            0
        ),
        Driver(
            "ocon",
            "Esteban",
            "Ocon",
            "France",
            LocalDate.parse("1996-09-17"),
            31,
            team("haas"),
            28,
            15,
            0
        ),
        Driver(
            "gasly",
            "Pierre",
            "Gasly",
            "France",
            LocalDate.parse("1996-02-07"),
            10,
            team("alpine"),
            20,
            16,
            0
        ),
        Driver(
            "tsunoda",
            "Yuki",
            "Tsunoda",
            "Japan",
            LocalDate.parse("2000-05-11"),
            22,
            team("red_bull"),
            20,
            17,
            0
        ),
        Driver(
            "bortoleto",
            "Gabriel",
            "Bortoleto",
            "Brazil",
            LocalDate.parse("2004-10-14"),
            5,
            team("sauber"),
            18,
            18,
            0
        ),
        Driver(
            "bearman",
            "Oliver",
            "Bearman",
            "Great Britain",
            LocalDate.parse("2005-05-08"),
            87,
            team("haas"),
            16,
            19,
            0
        ),
        Driver(
            "colapinto",
            "Franco",
            "Colapinto",
            "Argentina",
            LocalDate.parse("2003-05-27"),
            43,
            team("alpine"),
            0,
            20,
            0
        ),
        Driver(
            "doohan",
            "Jack",
            "Doohan",
            "Australia",
            LocalDate.parse("2003-01-20"),
            7,
            team("alpine"),
            0,
            21,
            0
        )
    )

    fun getDrivers(): List<Driver> = drivers
    fun getDriverById(id: String): Driver? = drivers.find { it.id == id }

    private fun team(id: String) = teams.first { it.id == id }
}
