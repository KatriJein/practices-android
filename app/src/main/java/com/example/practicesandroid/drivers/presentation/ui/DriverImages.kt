package com.example.practicesandroid.drivers.presentation.ui

object DriverImages {
    private val driverImages = mapOf(
        "piastri" to "https://i.pinimg.com/originals/22/55/49/22554991aae568ad7f91bdec337df917.jpg",
        "norris" to "https://i.pinimg.com/736x/c6/4a/a5/c64aa51a8ca6087e0669accbbf09f6da.jpg",
        "max_verstappen" to "https://avatars.mds.yandex.net/i?id=4248a1856ded0268e4f19ab4d8a6ac28_l-4578614-images-thumbs&n=13",
        "russell" to "https://d3cm515ijfiu6w.cloudfront.net/wp-content/uploads/2022/02/23061012/george-russell-mercedes-twitter.jpg",
        "leclerc" to "https://i.pinimg.com/originals/c0/b2/95/c0b295ddfb5bba2fb931232df25f469b.jpg",
        "hamilton" to "https://f1report.ru/img/newsf1/2024/02/303120140910.jpg",
        "antonelli" to "https://cdn1.f1oversteer.com/uploads/37/2024/10/GettyImages-2173086818-1536x1024.jpg",
        "albon" to "https://www.autoracing1.com/wp-content/uploads/2024/mugshots/alex-albon6.jpg",
        "hadjar" to "https://www.automotorsport.az/wp-content/uploads/2024/07/DSC_3575_D1K3lCaz.jpg?v=1720465617",
        "hulkenberg" to "https://img.championat.com/s/1350x900/news/big/y/b/hyulkenberg-nazval-vozmozhnuyu-prichinu-vyzvavshuyu-ego-diskvalifikaciyu-v-bahrejne_1744958503735086107.jpg",
        "stroll" to "https://cdn.images.express.co.uk/img/dynamic/73/1200x712/4598805.jpg",
        "sainz" to "https://www.motorsportweek.com/wp-content/uploads/2025/03/Carlos-Sainz-Williams-2.webp",
        "lawson" to "https://cdn-3.motorsport.com/images/amp/0qXDwqy6/s6/liam-lawson-carlin-1.jpg",
        "alonso" to "https://cdnn21.img.ria.ru/images/07e7/03/0d/1857571233_0:0:2048:1152_1920x1080_80_0_0_6d59fac2e1a0cfc6ad7466d515fa727f.jpg",
        "ocon" to "https://img.championat.com/s/1350x900/news/big/m/s/esteban-okon-propustit-pervuyu-trenirovku-gran-pri-ispanii_1748450032976884147.jpg",
        "gasly" to "https://cdn-5.motorsport.com/images/amp/27vmJBm0/s6/pierre-gasly-alpine-1.jpg",
        "tsunoda" to "https://avatars.mds.yandex.net/i?id=eeb5ba03f9dbeccb6478d49a61a002ce_l-12635432-images-thumbs&n=13",
        "bortoleto" to "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1yVECA.img?w=1200&h=675&m=4&q=79",
        "bearman" to "https://media.zenfs.com/en/motorsport_articles_445/2ad0d0b43056a4081b68cb00c42e82ec",
        "colapinto" to "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AA1EHbu3.img?w=1200&h=675&m=4&q=89",
        "doohan" to "https://www.topgear.com/sites/default/files/2025/05/BWT%20Alpine%20F1%20Team%20Reserve%20Driver%20Jack%20Doohan%20to%20drive%20A523%20in%20Mexico%20City%20and%20Abu%20Dhabi%20FP1%20sessions.jpg"
    )

    private const val DEFAULT = "https://www.manageengine.com/images/speaker-placeholder.png"

    fun getImageUrl(driverId: String?): String {
        return driverImages[driverId] ?: DEFAULT
    }
}