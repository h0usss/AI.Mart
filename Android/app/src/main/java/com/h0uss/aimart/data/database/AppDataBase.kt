package com.h0uss.aimart.data.database

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.h0uss.aimart.R
import com.h0uss.aimart.data.converter.BigDecimalConverter
import com.h0uss.aimart.data.converter.ListIntConverter
import com.h0uss.aimart.data.converter.ListStringConverter
import com.h0uss.aimart.data.converter.LocalDateTimeConverter
import com.h0uss.aimart.data.dao.ChatDao
import com.h0uss.aimart.data.dao.FeedbackDao
import com.h0uss.aimart.data.dao.MessageDao
import com.h0uss.aimart.data.dao.OrderDao
import com.h0uss.aimart.data.dao.PortfolioDao
import com.h0uss.aimart.data.dao.ProductDao
import com.h0uss.aimart.data.dao.SearchHintDao
import com.h0uss.aimart.data.dao.UserDao
import com.h0uss.aimart.data.dao.UserSellInfoDao
import com.h0uss.aimart.data.emun.OrderStatus
import com.h0uss.aimart.data.emun.ProductStatus
import com.h0uss.aimart.data.entity.ChatEntity
import com.h0uss.aimart.data.entity.FeedbackEntity
import com.h0uss.aimart.data.entity.FeedbackWithUserReferenceView
import com.h0uss.aimart.data.entity.MessageEntity
import com.h0uss.aimart.data.entity.OrderEntity
import com.h0uss.aimart.data.entity.PortfolioItemEntity
import com.h0uss.aimart.data.entity.PortfolioTagEntity
import com.h0uss.aimart.data.entity.ProductEntity
import com.h0uss.aimart.data.entity.ProductTagEntity
import com.h0uss.aimart.data.entity.SearchHintEntity
import com.h0uss.aimart.data.entity.TagEntity
import com.h0uss.aimart.data.entity.TransactionEntity
import com.h0uss.aimart.data.entity.UserEntity
import com.h0uss.aimart.data.entity.UserSellInfoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import kotlin.random.Random

@TypeConverters(
    value = [
        LocalDateTimeConverter::class,
        ListStringConverter::class,
        BigDecimalConverter::class,
        ListIntConverter::class,
    ]
)
@Database(
    entities = [
        PortfolioItemEntity::class,
        PortfolioTagEntity::class,
        UserSellInfoEntity::class,
        TransactionEntity::class,
        ProductTagEntity::class,
        SearchHintEntity::class,
        FeedbackEntity::class,
        MessageEntity::class,
        ProductEntity::class,
        OrderEntity::class,
        UserEntity::class,
        ChatEntity::class,
        TagEntity::class,
    ],
    views = [
        FeedbackWithUserReferenceView::class,
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun searchHintDao(): SearchHintDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun feedbackDao(): FeedbackDao
    abstract fun userSellInfoDao(): UserSellInfoDao
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        fun getDataBase(context: Context, scope: CoroutineScope): AppDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = AppDataBase::class.java,
                    name = "aimart_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            scope.launch(Dispatchers.IO) {
                INSTANCE?.let { database ->
                    populateDatabase(
                        userDao = database.userDao(),
                        productDao = database.productDao(),
                        userSellInfoDao = database.userSellInfoDao(),
                        portfolioDao = database.portfolioDao(),
                        orderDao = database.orderDao(),
                        feedbackDao = database.feedbackDao(),
                        chatDao = database.chatDao(),
                        messageDao = database.messageDao(),
                    )
                } ?: Log.e("AI.MartDB", "Fatal: AppDataBase INSTANCE was null after creation.")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun populateDatabase(
    userDao: UserDao,
    productDao: ProductDao,
    userSellInfoDao: UserSellInfoDao,
    portfolioDao: PortfolioDao,
    orderDao: OrderDao,
    feedbackDao: FeedbackDao,
    chatDao: ChatDao,
    messageDao: MessageDao,
) {
    val usersIds: List<Long> = fillUsers(userDao, userSellInfoDao)
    val p = fillProduct(productDao, usersIds)
    fillPortfolio(portfolioDao, usersIds)
    val o = fillOrders(orderDao = orderDao, usersIds, p)
    fillFeedback(feedbackDao, o)
    feelChat(chatDao, messageDao, p, orderDao)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fillUsers(
    userDao: UserDao,
    userSellInfoDao: UserSellInfoDao
): List<Long> {

    val rates = listOf(
        1f, 2f, 3f, 4f, 5f,
        0.1f, 0.2f, 3.1f, 4.4f, 5f,
    )
    val avatars = listOf(
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_10}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_1}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_2}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_3}",
        "android.resource://com.h0uss.aimart/${R.drawable.seller}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_0}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_1}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_2}",
        "android.resource://com.h0uss.aimart/${R.drawable.avatar_3}",
        "android.resource://com.h0uss.aimart/${R.drawable.seller}"
    )
    val names = listOf(
        "im", "Комбуча", "Няшка", "Дураша", "Нюша",
        "Авраам", "Кокакола", "Кофи", "Привет", "Крош",
    )
    val emails = listOf(
        "im@gmail.com",
        "Komb@gmail.com",
        "Nua@gmail.com",
        "dur@gmail.com",
        "nusha@gmail.com",
        "avraa@gmail.com",
        "pipisi@gmail.com",
        "coFe@gmail.com",
        "hi@gmail.com",
        "krr@gmail.com",
    )
    val balances = listOf(
        100f, 200f, 300f, 400f, 500f,
        100f, 200f, 300f, 400f, 500f,
    )
    val nickNames = listOf(
        "aas", "Popi", "ASdw", "NNkl", "34J",
        "GH84", "DDfs", "Koki", "vsdD", "3re",
    )
    val isSellers = listOf(
        true, true, true, true, true,
        false, false, false, false, false,
    )

    val initialUsers: List<UserEntity> = buildList {
        for (i in 0..names.size - 1) {
            add(
                UserEntity(
                    rate = rates[i],
                    avatar = avatars[i],
                    name = names[i],
                    email = emails[i],
                    balance = balances[i],
                    nickName = nickNames[i],
                    isSeller = isSellers[i],
                    passwordHash = BCrypt.hashpw("aaaaaaaa", BCrypt.gensalt(12)),
                    registerTime = LocalDateTime.now()
                )
            )
        }
    }

    val userIds = userDao.insertAll(initialUsers)

    val professions = listOf(
        "Нейрохудожник",
        "AI-иллюстратор",
        "Создатель концепт-артов",
        "Дизайнер персонажей",
        "Генеративный художник"
    )
    val abouts = listOf(
        "Создаю уникальные портреты в стиле фэнтези с помощью нейронных сетей. Воплощу любую вашу идею в жизнь.",
        "Специализируюсь на киберпанк-пейзажах и сложных абстракциях. Работаю быстро и качественно.",
        "Разрабатываю концепт-арты для игр и кино. Открыт для интересных и сложных проектов.",
        "Придумываю и рисую запоминающихся персонажей в аниме-стиле. Люблю яркие и детализированные работы.",
        "Экспериментирую с генеративным искусством, создавая фрактальные и органические паттерны."
    )
    val skillss = listOf(
        listOf("Midjourney", "Photoshop", "Портреты", "Фэнтези"),
        listOf("Stable Diffusion", "Киберпанк", "Пейзажи", "Архитектура", "Blender"),
        listOf("Концепт-арт", "Sci-Fi", "Игровой дизайн", "DALL-E 3"),
        listOf("Дизайн персонажей", "Аниме", "Иллюстрация", "Clip Studio Paint"),
        listOf("Генеративное искусство", "Абстракции", "Processing", "TouchDesigner")
    )

    val initialUsersSell: List<UserSellInfoEntity> = buildList {
        for (i in 0..4) {
            add(
                UserSellInfoEntity(
                    about = abouts[i],
                    skills = skillss[i],
                    profession = professions[i],
                    userId = userIds[i]
                )
            )
        }
    }

    userSellInfoDao.insertAll(initialUsersSell)
    return userIds
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fillProduct(productDao: ProductDao, users: List<Long>): List<Long> {

    val productArtTypes = listOf("Портрет", "Пейзаж", "Абстракция", "Фэнтези", "Киберпанк", "Аниме")
    val productImageIds = listOf(
        R.drawable.add_0, R.drawable.background, R.drawable.background_0
    )

    val names = List(50) { "${productArtTypes.random()} #${it + 1}" }
    val prices = List(50) { Random.nextInt(5, 500).toFloat() }
    val descriptions =
        List(50) { "Высококачественное цифровое изображение, созданное с помощью нейронных сетей. Идеально подходит для печати или использования в качестве аватара. Стиль: ${productArtTypes.random()}." }
    val createDates = List(50) { LocalDateTime.now().minusDays(Random.nextLong(0, 365)) }
    val productStatuses = List(50) { ProductStatus.entries.toTypedArray().random() }

    val initialProduct: List<ProductEntity> = buildList {
        for (i in 0..49) {
            add(
                ProductEntity(
                    name = names[i],
                    imagesUrl = List(4) { "android.resource://com.h0uss.aimart/${productImageIds.random()}" },
                    price = prices[i],
                    description = descriptions[i],
                    createDate = createDates[i],
                    productStatus = productStatuses[i],
                    userId = users[i % 5]
                )
            )
        }
    }

    return productDao.insertAll(initialProduct)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fillPortfolio(portfolioDao: PortfolioDao, userIds: List<Long>) {
    val portfolioImageIds = listOf(
        R.drawable.add_0, R.drawable.background, R.drawable.background_0
    )
    val artStyles = listOf("Fantasy", "Cyberpunk", "Anime", "Realism", "Abstract", "Sci-Fi")
    val artTypes = listOf("Character", "Environment", "Creature", "Prop", "Vehicle")

    val initialPortfolio: List<PortfolioItemEntity> = buildList {
        val sellerIds = userIds.take(5)
        sellerIds.forEach { sellerId ->
            val itemCount = Random.nextInt(1, 4)
            repeat(itemCount) {
                val style = artStyles.random()
                val type = artTypes.random()
                add(
                    PortfolioItemEntity(
                        price = Random.nextInt(50, 1000).toFloat(),
                        media = listOf(
                            portfolioImageIds.random(),
                            portfolioImageIds.random(),
                            portfolioImageIds.random()
                        ),
                        title = "$style $type",
                        description = "A custom piece of art in the $style style, depicting a unique $type. Created using advanced AI generation techniques and manual refinement.",
                        createTime = LocalDateTime.now().minusDays(Random.nextLong(0, 730)),
                        userId = sellerId
                    )
                )
            }
        }
    }
    portfolioDao.insertAll(initialPortfolio)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fillOrders(
    orderDao: OrderDao,
    userIds: List<Long>,
    productIds: List<Long>
): List<Long> {
    val sellerIds = userIds.take(5)
    val buyerIds = userIds.drop(5)
    val statuses = OrderStatus.entries.toTypedArray()

    val initialOrder: List<OrderEntity> = buildList {
        for (i in 0..49) {
            val status = statuses[i % statuses.size]
            val startDate = LocalDateTime.now().minusDays(Random.nextLong(1, 45))
            val completionDate = if (status == OrderStatus.COMPLETE) {
                startDate.plusDays(Random.nextLong(1, 6))
            } else {
                null
            }

            add(
                OrderEntity(
                    price = Random.nextInt(10, 500).toFloat(),
                    status = status,
                    deadline = startDate.plusDays(Random.nextLong(7, 60)),
                    startDate = startDate,
                    completionDate = completionDate,
                    buyerId = buyerIds.random(),
                    sellerId = sellerIds.random(),
                    productId = productIds.random(),
                    description = LoremIpsum(Random.nextInt(20, 50)).values.joinToString("") { it }
                )
            )
        }
    }

    return orderDao.insertAll(initialOrder)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fillFeedback(feedbackDao: FeedbackDao, orderIds: List<Long>) {
    val positiveTexts = listOf(
        "Отличная работа!",
        "Все супер, рекомендую!",
        "Очень быстро и качественно.",
        "Результат превзошел ожидания!",
        "Буду обращаться еще."
    )
    val neutralTexts =
        listOf("Нормально.", "Работа выполнена.", "В целом, неплохо.", "Соответствует описанию.")
    val negativeTexts = listOf(
        "Были некоторые проблемы.",
        "Не совсем то, что я ожидал.",
        "Пришлось вносить много правок.",
        "Затянули со сроками."
    )

    val initialFeedback: List<FeedbackEntity> = buildList {
        orderIds.forEach { orderId ->
            val starCount = Random.nextInt(1, 6)
            val text = when (starCount) {
                5 -> positiveTexts.random()
                4 -> positiveTexts.random()
                3 -> neutralTexts.random()
                else -> negativeTexts.random()
            }
            add(
                FeedbackEntity(
                    text = text,
                    starCount = starCount,
                    timestamp = LocalDateTime.now().minusDays(Random.nextLong(0, 10)),
                    orderId = orderId
                )
            )
        }
    }

    feedbackDao.insertAll(initialFeedback)
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun feelChat(
    chatDao: ChatDao,
    messageDao: MessageDao,
    orders: List<Long>,
    ordersDao: OrderDao
) {
    val initialChats: List<ChatEntity> = buildList {
        for (i in 0 until 10) {
            val order = ordersDao.getOrderById(orders[i]).first()

            val myId = if (i % 2 == 0) order.buyerId else order.sellerId
            val otherId = if (i % 2 == 0) order.sellerId else order.buyerId

            add(
                ChatEntity(
                    fUserId = myId,
                    sUserId = otherId,
                    orderId = orders[i],
                    createdAt = LocalDateTime.now(),
                )
            )
        }
    }

    chatDao.insertAll(initialChats)
    val chats = chatDao.getAll()

    val messages = List(30) {
        LoremIpsum(Random.nextInt(1, 30)).values.joinToString("") { it }
    }

    val initialMessage: List<MessageEntity> = buildList {
        chats.first().forEach { chat ->
            repeat(Random.nextInt(1, 15)) {
                add(
                    MessageEntity(
                        chatId = chat.id,
                        senderId = if (Random.nextBoolean()) chat.fUserId else chat.sUserId,
                        message = messages[Random.nextInt(0, messages.size - 1)],
                        createdAt = LocalDateTime.now(),
                    )
                )
            }
        }
    }
    messageDao.insertAll(initialMessage)

}