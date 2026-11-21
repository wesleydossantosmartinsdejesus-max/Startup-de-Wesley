package com.example.ciclomultfood.data.repository

import com.example.ciclomultfood.data.dao.CommunityPostDao
import com.example.ciclomultfood.data.dao.ListingDao
import com.example.ciclomultfood.data.dao.LocalMarketDao
import com.example.ciclomultfood.data.dao.UserPreferencesDao
import com.example.ciclomultfood.data.model.CommunityPostEntity
import com.example.ciclomultfood.data.model.ListingEntity
import com.example.ciclomultfood.data.model.LocalMarketEntity
import com.example.ciclomultfood.data.model.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

class CiclomultRepository(
    private val listingDao: ListingDao,
    private val communityPostDao: CommunityPostDao,
    private val userPreferencesDao: UserPreferencesDao,
    private val localMarketDao: LocalMarketDao
) {
    val listings: Flow<List<ListingEntity>> = listingDao.getAllVisibleListings()
    val communityPosts: Flow<List<CommunityPostEntity>> = communityPostDao.getAllVisiblePosts()
    val userPreferences: Flow<UserPreferencesEntity?> = userPreferencesDao.getUserPreferences()
    val localMarkets: Flow<List<LocalMarketEntity>> = localMarketDao.getAllLocalMarkets()

    suspend fun seedInitialDataIfNeeded() {
        if (userPreferencesDao.getUserPreferencesSync() == null) {
            userPreferencesDao.insertOrUpdate(UserPreferencesEntity())
        }

        if (listingDao.getCount() == 0) {
            val initialListings = listOf(
                ListingEntity(
                    title = "2 kg de Laranja Pera Fresca",
                    category = "COMPRA_VENDA",
                    subCategory = "Frutas",
                    description = "Colhidas hoje no meu pequeno quintal urbano na Zona Sul. Sem agrotóxicos! Muito suculentas e doces.",
                    quantity = "2 kg",
                    price = 4.50,
                    sellerName = "Seu Zé Hortaliças",
                    sellerNeighborhood = "Capão Redondo, SP (400m)",
                    sellerWhatsApp = "5511988887777",
                    sellerMapAddress = "Rua Comendador Sant'Anna, 300 - Capão Redondo, São Paulo - SP",
                    sellerIsPro = true,
                    imageUrl = "img_fresh_produce_1785470112199"
                ),
                ListingEntity(
                    title = "3 Pés de Alface Crespa Orgânica",
                    category = "COMPRA_VENDA",
                    subCategory = "Hortaliças",
                    description = "Alface verdinha e super crocante cultivada na horta comunitária local. Retirada rápida.",
                    quantity = "3 pés",
                    price = 3.00,
                    sellerName = "Ana Paula Cultivos",
                    sellerNeighborhood = "Heliópolis, SP (800m)",
                    sellerWhatsApp = "5511977776666",
                    sellerMapAddress = "Estrada das Lágrimas, 1200 - Heliópolis, São Paulo - SP",
                    sellerIsPro = false,
                    imageUrl = "img_fresh_produce_1785470112199"
                ),
                ListingEntity(
                    title = "Doação: 10 kg de Bananas Prata Maduras",
                    category = "DOACAO",
                    subCategory = "Frutas",
                    description = "Própria para consumo imediato ou fazer doces/bolo em cozinhas comunitárias e ONGs parceiras.",
                    quantity = "10 kg",
                    price = 0.00,
                    sellerName = "Sacolão do Bairro",
                    sellerNeighborhood = "Jardim Ângela, SP (1.2 km)",
                    sellerWhatsApp = "5511966665555",
                    sellerMapAddress = "Estrada do M'Boi Mirim, 4500 - Jardim Ângela, São Paulo - SP",
                    sellerIsPro = true,
                    imageUrl = "img_food_donation_1785470135793"
                ),
                ListingEntity(
                    title = "Excedente de Pães e Broas do Dia",
                    category = "DOACAO",
                    subCategory = "Padaria",
                    description = "20 pães e broas frescas do dia doados para famílias cadastradas ou lideranças locais.",
                    quantity = "20 un",
                    price = 0.00,
                    sellerName = "Padaria Comunitária Esperança",
                    sellerNeighborhood = "Capão Redondo, SP (600m)",
                    sellerWhatsApp = "5511955554444",
                    sellerIsPro = true,
                    imageUrl = "img_food_donation_1785470135793"
                ),
                ListingEntity(
                    title = "Saco de Cascas de Frutas e Restos Vegetais",
                    category = "RECICLAGEM_ORGANICA",
                    subCategory = "Compostagem",
                    description = "15kg de matéria orgânica limpa e selecionada para minhocários, usinas de compostagem ou adubo.",
                    quantity = "15 kg",
                    price = 0.00,
                    sellerName = "Hortifruti São José",
                    sellerNeighborhood = "Grajaú, SP (1.5 km)",
                    sellerWhatsApp = "5511944443333",
                    sellerIsPro = false,
                    imageUrl = "img_compost_organic_1785470123756"
                ),
                ListingEntity(
                    title = "Esterco Curtido de Galinha para Solo",
                    category = "RECICLAGEM_ORGANICA",
                    subCategory = "Adubo Natural",
                    description = "Adubo orgânico rico em nitrogênio para fortificar o solo do seu quintal ou canteiro.",
                    quantity = "1 Saco 10kg",
                    price = 8.00,
                    sellerName = "Sítio Recanto Verde",
                    sellerNeighborhood = "Parelheiros, SP (2.8 km)",
                    sellerWhatsApp = "5511933332222",
                    sellerIsPro = true,
                    imageUrl = "img_compost_organic_1785470123756"
                )
            )
            listingDao.insertAll(initialListings)
        }

        if (communityPostDao.getCount() == 0) {
            val initialPosts = listOf(
                CommunityPostEntity(
                    authorName = "João do Cultivo Urbano",
                    authorNeighborhood = "Brasilândia, SP",
                    type = "COMO_CULTIVO",
                    title = "Horta em Caixotes de Feira sem Custo! 🥬",
                    content = "Dica para quem tem pouco espaço na periferia! Usei caixotes de feira forrados com sacos de ráfia perfurados. Colhi minha couve fresquinha hoje!",
                    mediaUrl = "img_fresh_produce_1785470112199",
                    isVideo = false,
                    likesCount = 42,
                    authorIsPro = true
                ),
                CommunityPostEntity(
                    authorName = "Cozinha Comunitária Solidária",
                    authorNeighborhood = "Paraisópolis, SP",
                    type = "CORRENTE_BEM",
                    title = "40 Lanches distribuídos hoje! 🥖❤️",
                    content = "Através das doações conectadas pelo Ciclomult_Food, garantimos o lanche da tarde de dezenas de crianças da comunidade. Zero desperdício!",
                    mediaUrl = "img_food_donation_1785470135793",
                    isVideo = false,
                    likesCount = 89,
                    authorIsPro = true
                ),
                CommunityPostEntity(
                    authorName = "Maria do Minhocário",
                    authorNeighborhood = "Campo Limpo, SP",
                    type = "COMO_CULTIVO",
                    title = "Adubo Líquido Caseiro na Garrafa PET 🪴",
                    content = "Vídeo rápido ensinando a transformar borra de café e cascas em biofertilizante rico! Clique para reproduzir no seu tempo (economia de dados 3G/4G).",
                    mediaUrl = "img_compost_organic_1785470123756",
                    isVideo = true,
                    likesCount = 61,
                    authorIsPro = false
                ),
                CommunityPostEntity(
                    authorName = "Rede Ciclomult • Ciência Alimentar",
                    authorNeighborhood = "Pesquisa Global & Brasil",
                    type = "CORRENTE_BEM",
                    title = "📊 Pesquisa ONU 2024: 1,05 Bilhão de Toneladas de Alimentos Desperdiçadas",
                    content = "Segundo dados de pesquisa oficial da ONU (PNUMA) e EMBRAPA, cada brasileiro descarta em média 79kg de comida própria para consumo por ano. Preservar, congelar e doar o excedente evita emissões de metano e alimenta quem precisa!",
                    mediaUrl = "img_food_donation_1785470135793",
                    isVideo = false,
                    likesCount = 142,
                    authorIsPro = true
                ),
                CommunityPostEntity(
                    authorName = "Embrapa & Hortas Urbanas",
                    authorNeighborhood = "Capão Redondo, SP",
                    type = "COMO_CULTIVO",
                    title = "🌾 Dica de Preservação: Aumente a durabilidade das verduras em 3x",
                    content = "Pesquisas de conservação indicam: secar as folhas verdes antes de refrigerar e utilizar branqueamento (água quente + choque térmico em água gelada) preserva 95% dos nutrientes por até 15 dias sem estragar!",
                    mediaUrl = "img_fresh_produce_1785470112199",
                    isVideo = false,
                    likesCount = 98,
                    authorIsPro = true
                )
            )
            communityPostDao.insertAll(initialPosts)
        }

        if (localMarketDao.getCount() == 0) {
            val initialMarkets = listOf(
                LocalMarketEntity(
                    name = "Feira Agroecológica da Periferia",
                    location = "Praça Central do Bairro - R. das Flores, 120, SP",
                    latitude = -23.5805,
                    longitude = -46.6433,
                    availableItems = "Hortaliças orgânicas, Ervas medicinais, Ovos caipiras, Mudas orgânicas",
                    operatingHours = "Sábados, das 07:00 às 12:30",
                    organizerContact = "5511987654321"
                ),
                LocalMarketEntity(
                    name = "Feira Orgânica & Troca Sustentável",
                    location = "Parque Comunitário das Águas - Av. Verde, 450, SP",
                    latitude = -23.5905,
                    longitude = -46.6533,
                    availableItems = "Frutas da estação, Mel natural, Geleias artesanais, Composto para horta",
                    operatingHours = "Domingos, das 08:00 às 13:00",
                    organizerContact = "5511988887777"
                ),
                LocalMarketEntity(
                    name = "Feira Livre EcoVila & Horta Comunitária",
                    location = "Largo da Estação - R. do Comércio, 85, SP",
                    latitude = -23.5705,
                    longitude = -46.6333,
                    availableItems = "Legumes frescos, Pães artesanais de fermentação natural, Adubo de compostagem",
                    operatingHours = "Quartas e Sextas, das 15:00 às 19:00",
                    organizerContact = "5511977776666"
                )
            )
            localMarketDao.insertAll(initialMarkets)
        }
    }

    suspend fun addLocalMarket(market: LocalMarketEntity) {
        localMarketDao.insertMarket(market)
    }

    suspend fun syncLocalMarketsFromFirestore(markets: List<LocalMarketEntity>) {
        if (markets.isNotEmpty()) {
            localMarketDao.insertAll(markets)
        }
    }

    suspend fun acceptTerms() {
        userPreferencesDao.setAcceptedTerms(true)
    }

    suspend fun addCredits(amount: Int) {
        userPreferencesDao.addCredits(amount)
    }

    suspend fun consumeCredit() {
        userPreferencesDao.consumeCredit()
    }

    suspend fun setProStatus(isPro: Boolean) {
        userPreferencesDao.setProStatus(isPro)
    }

    suspend fun incrementImpact(kgSaved: Double, co2Evaded: Double, donationsCount: Int) {
        userPreferencesDao.incrementImpact(kgSaved, co2Evaded, donationsCount)
    }

    suspend fun updateUserProfile(name: String, handle: String, neighborhood: String, whatsApp: String, timestamp: Long) {
        userPreferencesDao.updateUserProfile(name, handle, neighborhood, whatsApp, timestamp)
    }

    suspend fun updateFollowedUsers(followed: String) {
        userPreferencesDao.updateFollowedUsers(followed)
    }

    suspend fun resetUserProfile(user: UserPreferencesEntity) {
        userPreferencesDao.insertOrUpdate(user)
    }

    suspend fun saveUserPreferences(user: UserPreferencesEntity) {
        userPreferencesDao.insertOrUpdate(user)
    }

    suspend fun addListing(listing: ListingEntity) {
        listingDao.insertListing(listing)
    }

    suspend fun reportListing(id: Int) {
        listingDao.reportListing(id)
    }

    suspend fun addPost(post: CommunityPostEntity) {
        communityPostDao.insertPost(post)
    }

    suspend fun toggleLikePost(id: Int, currentlyLiked: Boolean, currentLikes: Int) {
        val newLiked = !currentlyLiked
        val newCount = if (newLiked) currentLikes + 1 else (currentLikes - 1).coerceAtLeast(0)
        communityPostDao.updateLike(id, newLiked, newCount)
    }

    suspend fun reportPost(id: Int) {
        communityPostDao.reportPost(id)
    }
}
