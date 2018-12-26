package net.mindlevel.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Accomplishment.schema, AccomplishmentLike.schema, Category.schema, Challenge.schema, ChallengeCategory.schema, CustomDb.schema, Session.schema, User.schema, UserAccomplishment.schema, UserExtra.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Accomplishment
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(VARCHAR), Length(191,true)
   *  @param description Database column description SqlType(VARCHAR), Length(1024,true)
   *  @param image Database column image SqlType(VARCHAR), Length(191,true)
   *  @param challengeId Database column challenge_id SqlType(INT)
   *  @param score Database column score SqlType(INT), Default(0)
   *  @param created Database column created SqlType(BIGINT), Default(None)
   *  @param levelRestriction Database column level_restriction SqlType(INT), Default(Some(0))
   *  @param scoreRestriction Database column score_restriction SqlType(INT), Default(Some(0)) */
  case class AccomplishmentRow(id: Int, title: String, description: String, image: String, challengeId: Int, score: Int = 0, created: Option[Long] = None, levelRestriction: Option[Int] = Some(0), scoreRestriction: Option[Int] = Some(0))
  /** GetResult implicit for fetching AccomplishmentRow objects using plain SQL queries */
  implicit def GetResultAccomplishmentRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Long]], e3: GR[Option[Int]]): GR[AccomplishmentRow] = GR{
    prs => import prs._
    AccomplishmentRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<?[Long], <<?[Int], <<?[Int]))
  }
  /** Table description of table accomplishment. Objects of this class serve as prototypes for rows in queries. */
  class Accomplishment(_tableTag: Tag) extends profile.api.Table[AccomplishmentRow](_tableTag, None, "accomplishment") {
    def * = (id, title, description, image, challengeId, score, created, levelRestriction, scoreRestriction) <> (AccomplishmentRow.tupled, AccomplishmentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(title), Rep.Some(description), Rep.Some(image), Rep.Some(challengeId), Rep.Some(score), created, levelRestriction, scoreRestriction).shaped.<>({r=>import r._; _1.map(_=> AccomplishmentRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(VARCHAR), Length(191,true) */
    val title: Rep[String] = column[String]("title", O.Length(191,varying=true))
    /** Database column description SqlType(VARCHAR), Length(1024,true) */
    val description: Rep[String] = column[String]("description", O.Length(1024,varying=true))
    /** Database column image SqlType(VARCHAR), Length(191,true) */
    val image: Rep[String] = column[String]("image", O.Length(191,varying=true))
    /** Database column challenge_id SqlType(INT) */
    val challengeId: Rep[Int] = column[Int]("challenge_id")
    /** Database column score SqlType(INT), Default(0) */
    val score: Rep[Int] = column[Int]("score", O.Default(0))
    /** Database column created SqlType(BIGINT), Default(None) */
    val created: Rep[Option[Long]] = column[Option[Long]]("created", O.Default(None))
    /** Database column level_restriction SqlType(INT), Default(Some(0)) */
    val levelRestriction: Rep[Option[Int]] = column[Option[Int]]("level_restriction", O.Default(Some(0)))
    /** Database column score_restriction SqlType(INT), Default(Some(0)) */
    val scoreRestriction: Rep[Option[Int]] = column[Option[Int]]("score_restriction", O.Default(Some(0)))

    /** Foreign key referencing Challenge (database name fk_accomplishment_challenge) */
    lazy val challengeFk = foreignKey("fk_accomplishment_challenge", challengeId, Challenge)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Accomplishment */
  lazy val Accomplishment = new TableQuery(tag => new Accomplishment(tag))

  /** Entity class storing rows of table AccomplishmentLike
   *  @param username Database column username SqlType(VARCHAR), Length(191,true)
   *  @param accomplishmentId Database column accomplishment_id SqlType(INT)
   *  @param created Database column created SqlType(BIGINT)
   *  @param score Database column score SqlType(INT) */
  case class AccomplishmentLikeRow(username: String, accomplishmentId: Int, created: Long, score: Int)
  /** GetResult implicit for fetching AccomplishmentLikeRow objects using plain SQL queries */
  implicit def GetResultAccomplishmentLikeRow(implicit e0: GR[String], e1: GR[Int], e2: GR[Long]): GR[AccomplishmentLikeRow] = GR{
    prs => import prs._
    AccomplishmentLikeRow.tupled((<<[String], <<[Int], <<[Long], <<[Int]))
  }
  /** Table description of table accomplishment_like. Objects of this class serve as prototypes for rows in queries. */
  class AccomplishmentLike(_tableTag: Tag) extends profile.api.Table[AccomplishmentLikeRow](_tableTag, None, "accomplishment_like") {
    def * = (username, accomplishmentId, created, score) <> (AccomplishmentLikeRow.tupled, AccomplishmentLikeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), Rep.Some(accomplishmentId), Rep.Some(created), Rep.Some(score)).shaped.<>({r=>import r._; _1.map(_=> AccomplishmentLikeRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(VARCHAR), Length(191,true) */
    val username: Rep[String] = column[String]("username", O.Length(191,varying=true))
    /** Database column accomplishment_id SqlType(INT) */
    val accomplishmentId: Rep[Int] = column[Int]("accomplishment_id")
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column score SqlType(INT) */
    val score: Rep[Int] = column[Int]("score")

    /** Primary key of AccomplishmentLike (database name accomplishment_like_PK) */
    val pk = primaryKey("accomplishment_like_PK", (username, accomplishmentId))

    /** Foreign key referencing Accomplishment (database name fk_accomplishment_like_2) */
    lazy val accomplishmentFk = foreignKey("fk_accomplishment_like_2", accomplishmentId, Accomplishment)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name fk_accomplishment_like_1) */
    lazy val userFk = foreignKey("fk_accomplishment_like_1", username, User)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AccomplishmentLike */
  lazy val AccomplishmentLike = new TableQuery(tag => new AccomplishmentLike(tag))

  /** Entity class storing rows of table Category
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(VARCHAR), Length(191,true)
   *  @param image Database column image SqlType(VARCHAR), Length(191,true) */
  case class CategoryRow(id: Int, name: String, image: String)
  /** GetResult implicit for fetching CategoryRow objects using plain SQL queries */
  implicit def GetResultCategoryRow(implicit e0: GR[Int], e1: GR[String]): GR[CategoryRow] = GR{
    prs => import prs._
    CategoryRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table category. Objects of this class serve as prototypes for rows in queries. */
  class Category(_tableTag: Tag) extends profile.api.Table[CategoryRow](_tableTag, None, "category") {
    def * = (id, name, image) <> (CategoryRow.tupled, CategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(image)).shaped.<>({r=>import r._; _1.map(_=> CategoryRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(VARCHAR), Length(191,true) */
    val name: Rep[String] = column[String]("name", O.Length(191,varying=true))
    /** Database column image SqlType(VARCHAR), Length(191,true) */
    val image: Rep[String] = column[String]("image", O.Length(191,varying=true))
  }
  /** Collection-like TableQuery object for table Category */
  lazy val Category = new TableQuery(tag => new Category(tag))

  /** Entity class storing rows of table Challenge
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(VARCHAR), Length(191,true)
   *  @param description Database column description SqlType(VARCHAR), Length(1024,true)
   *  @param image Database column image SqlType(VARCHAR), Length(191,true)
   *  @param created Database column created SqlType(TIMESTAMP)
   *  @param creator Database column creator SqlType(VARCHAR), Length(191,true)
   *  @param validated Database column validated SqlType(BIT)
   *  @param levelRestriction Database column level_restriction SqlType(INT), Default(Some(0))
   *  @param scoreRestriction Database column score_restriction SqlType(INT), Default(Some(0)) */
  case class ChallengeRow(id: Int, title: String, description: String, image: String, created: java.sql.Timestamp, creator: String, validated: Boolean, levelRestriction: Option[Int] = Some(0), scoreRestriction: Option[Int] = Some(0))
  /** GetResult implicit for fetching ChallengeRow objects using plain SQL queries */
  implicit def GetResultChallengeRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Boolean], e4: GR[Option[Int]]): GR[ChallengeRow] = GR{
    prs => import prs._
    ChallengeRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[String], <<[Boolean], <<?[Int], <<?[Int]))
  }
  /** Table description of table challenge. Objects of this class serve as prototypes for rows in queries. */
  class Challenge(_tableTag: Tag) extends profile.api.Table[ChallengeRow](_tableTag, None, "challenge") {
    def * = (id, title, description, image, created, creator, validated, levelRestriction, scoreRestriction) <> (ChallengeRow.tupled, ChallengeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(title), Rep.Some(description), Rep.Some(image), Rep.Some(created), Rep.Some(creator), Rep.Some(validated), levelRestriction, scoreRestriction).shaped.<>({r=>import r._; _1.map(_=> ChallengeRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(VARCHAR), Length(191,true) */
    val title: Rep[String] = column[String]("title", O.Length(191,varying=true))
    /** Database column description SqlType(VARCHAR), Length(1024,true) */
    val description: Rep[String] = column[String]("description", O.Length(1024,varying=true))
    /** Database column image SqlType(VARCHAR), Length(191,true) */
    val image: Rep[String] = column[String]("image", O.Length(191,varying=true))
    /** Database column created SqlType(TIMESTAMP) */
    val created: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created")
    /** Database column creator SqlType(VARCHAR), Length(191,true) */
    val creator: Rep[String] = column[String]("creator", O.Length(191,varying=true))
    /** Database column validated SqlType(BIT) */
    val validated: Rep[Boolean] = column[Boolean]("validated")
    /** Database column level_restriction SqlType(INT), Default(Some(0)) */
    val levelRestriction: Rep[Option[Int]] = column[Option[Int]]("level_restriction", O.Default(Some(0)))
    /** Database column score_restriction SqlType(INT), Default(Some(0)) */
    val scoreRestriction: Rep[Option[Int]] = column[Option[Int]]("score_restriction", O.Default(Some(0)))

    /** Foreign key referencing User (database name fk_challenge_user) */
    lazy val userFk = foreignKey("fk_challenge_user", creator, User)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Challenge */
  lazy val Challenge = new TableQuery(tag => new Challenge(tag))

  /** Entity class storing rows of table ChallengeCategory
   *  @param challengeId Database column challenge_id SqlType(INT)
   *  @param categoryId Database column category_id SqlType(INT) */
  case class ChallengeCategoryRow(challengeId: Int, categoryId: Int)
  /** GetResult implicit for fetching ChallengeCategoryRow objects using plain SQL queries */
  implicit def GetResultChallengeCategoryRow(implicit e0: GR[Int]): GR[ChallengeCategoryRow] = GR{
    prs => import prs._
    ChallengeCategoryRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table challenge_category. Objects of this class serve as prototypes for rows in queries. */
  class ChallengeCategory(_tableTag: Tag) extends profile.api.Table[ChallengeCategoryRow](_tableTag, None, "challenge_category") {
    def * = (challengeId, categoryId) <> (ChallengeCategoryRow.tupled, ChallengeCategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(challengeId), Rep.Some(categoryId)).shaped.<>({r=>import r._; _1.map(_=> ChallengeCategoryRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column challenge_id SqlType(INT) */
    val challengeId: Rep[Int] = column[Int]("challenge_id")
    /** Database column category_id SqlType(INT) */
    val categoryId: Rep[Int] = column[Int]("category_id")

    /** Primary key of ChallengeCategory (database name challenge_category_PK) */
    val pk = primaryKey("challenge_category_PK", (challengeId, categoryId))

    /** Foreign key referencing Category (database name fk_challenge_category_2) */
    lazy val categoryFk = foreignKey("fk_challenge_category_2", categoryId, Category)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Challenge (database name fk_challenge_category_1) */
    lazy val challengeFk = foreignKey("fk_challenge_category_1", challengeId, Challenge)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table ChallengeCategory */
  lazy val ChallengeCategory = new TableQuery(tag => new ChallengeCategory(tag))

  /** Entity class storing rows of table CustomDb
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param pass Database column pass SqlType(VARCHAR), Length(191,true)
   *  @param db Database column db SqlType(VARCHAR), Length(191,true) */
  case class CustomDbRow(id: Int, pass: String, db: String)
  /** GetResult implicit for fetching CustomDbRow objects using plain SQL queries */
  implicit def GetResultCustomDbRow(implicit e0: GR[Int], e1: GR[String]): GR[CustomDbRow] = GR{
    prs => import prs._
    CustomDbRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table custom_db. Objects of this class serve as prototypes for rows in queries. */
  class CustomDb(_tableTag: Tag) extends profile.api.Table[CustomDbRow](_tableTag, None, "custom_db") {
    def * = (id, pass, db) <> (CustomDbRow.tupled, CustomDbRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(pass), Rep.Some(db)).shaped.<>({r=>import r._; _1.map(_=> CustomDbRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column pass SqlType(VARCHAR), Length(191,true) */
    val pass: Rep[String] = column[String]("pass", O.Length(191,varying=true))
    /** Database column db SqlType(VARCHAR), Length(191,true) */
    val db: Rep[String] = column[String]("db", O.Length(191,varying=true))
  }
  /** Collection-like TableQuery object for table CustomDb */
  lazy val CustomDb = new TableQuery(tag => new CustomDb(tag))

  /** Entity class storing rows of table Session
   *  @param username Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true)
   *  @param session Database column session SqlType(VARCHAR), Length(191,true), Default(None) */
  case class SessionRow(username: String, session: Option[String] = None)
  /** GetResult implicit for fetching SessionRow objects using plain SQL queries */
  implicit def GetResultSessionRow(implicit e0: GR[String], e1: GR[Option[String]]): GR[SessionRow] = GR{
    prs => import prs._
    SessionRow.tupled((<<[String], <<?[String]))
  }
  /** Table description of table session. Objects of this class serve as prototypes for rows in queries. */
  class Session(_tableTag: Tag) extends profile.api.Table[SessionRow](_tableTag, None, "session") {
    def * = (username, session) <> (SessionRow.tupled, SessionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), session).shaped.<>({r=>import r._; _1.map(_=> SessionRow.tupled((_1.get, _2)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true) */
    val username: Rep[String] = column[String]("username", O.PrimaryKey, O.Length(191,varying=true))
    /** Database column session SqlType(VARCHAR), Length(191,true), Default(None) */
    val session: Rep[Option[String]] = column[Option[String]]("session", O.Length(191,varying=true), O.Default(None))

    /** Foreign key referencing User (database name fk_session_user) */
    lazy val userFk = foreignKey("fk_session_user", username, User)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Session */
  lazy val Session = new TableQuery(tag => new Session(tag))

  /** Entity class storing rows of table User
   *  @param username Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true)
   *  @param description Database column description SqlType(VARCHAR), Length(1024,true), Default(None)
   *  @param image Database column image SqlType(VARCHAR), Length(191,true), Default(Some(user.jpg))
   *  @param score Database column score SqlType(INT), Default(Some(0))
   *  @param level Database column level SqlType(INT), Default(Some(0))
   *  @param created Database column created SqlType(BIGINT)
   *  @param lastActive Database column last_active SqlType(BIGINT), Default(None) */
  case class UserRow(username: String, description: Option[String] = None, image: Option[String] = Some("user.jpg"), score: Option[Int] = Some(0), level: Option[Int] = Some(0), created: Long, lastActive: Option[Long] = None)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[String], e1: GR[Option[String]], e2: GR[Option[Int]], e3: GR[Long], e4: GR[Option[Long]]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[String], <<?[String], <<?[String], <<?[Int], <<?[Int], <<[Long], <<?[Long]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, None, "user") {
    def * = (username, description, image, score, level, created, lastActive) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), description, image, score, level, Rep.Some(created), lastActive).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2, _3, _4, _5, _6.get, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true) */
    val username: Rep[String] = column[String]("username", O.PrimaryKey, O.Length(191,varying=true))
    /** Database column description SqlType(VARCHAR), Length(1024,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1024,varying=true), O.Default(None))
    /** Database column image SqlType(VARCHAR), Length(191,true), Default(Some(user.jpg)) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(191,varying=true), O.Default(Some("user.jpg")))
    /** Database column score SqlType(INT), Default(Some(0)) */
    val score: Rep[Option[Int]] = column[Option[Int]]("score", O.Default(Some(0)))
    /** Database column level SqlType(INT), Default(Some(0)) */
    val level: Rep[Option[Int]] = column[Option[Int]]("level", O.Default(Some(0)))
    /** Database column created SqlType(BIGINT) */
    val created: Rep[Long] = column[Long]("created")
    /** Database column last_active SqlType(BIGINT), Default(None) */
    val lastActive: Rep[Option[Long]] = column[Option[Long]]("last_active", O.Default(None))
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))

  /** Entity class storing rows of table UserAccomplishment
   *  @param username Database column username SqlType(VARCHAR), Length(191,true)
   *  @param accomplishmentId Database column accomplishment_id SqlType(INT) */
  case class UserAccomplishmentRow(username: String, accomplishmentId: Int)
  /** GetResult implicit for fetching UserAccomplishmentRow objects using plain SQL queries */
  implicit def GetResultUserAccomplishmentRow(implicit e0: GR[String], e1: GR[Int]): GR[UserAccomplishmentRow] = GR{
    prs => import prs._
    UserAccomplishmentRow.tupled((<<[String], <<[Int]))
  }
  /** Table description of table user_accomplishment. Objects of this class serve as prototypes for rows in queries. */
  class UserAccomplishment(_tableTag: Tag) extends profile.api.Table[UserAccomplishmentRow](_tableTag, None, "user_accomplishment") {
    def * = (username, accomplishmentId) <> (UserAccomplishmentRow.tupled, UserAccomplishmentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), Rep.Some(accomplishmentId)).shaped.<>({r=>import r._; _1.map(_=> UserAccomplishmentRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(VARCHAR), Length(191,true) */
    val username: Rep[String] = column[String]("username", O.Length(191,varying=true))
    /** Database column accomplishment_id SqlType(INT) */
    val accomplishmentId: Rep[Int] = column[Int]("accomplishment_id")

    /** Primary key of UserAccomplishment (database name user_accomplishment_PK) */
    val pk = primaryKey("user_accomplishment_PK", (username, accomplishmentId))

    /** Foreign key referencing Accomplishment (database name fk_user_accomplishment_2) */
    lazy val accomplishmentFk = foreignKey("fk_user_accomplishment_2", accomplishmentId, Accomplishment)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing User (database name fk_user_accomplishment_1) */
    lazy val userFk = foreignKey("fk_user_accomplishment_1", username, User)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserAccomplishment */
  lazy val UserAccomplishment = new TableQuery(tag => new UserAccomplishment(tag))

  /** Entity class storing rows of table UserExtra
   *  @param username Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true)
   *  @param password Database column password SqlType(VARCHAR), Length(191,true)
   *  @param email Database column email SqlType(VARCHAR), Length(191,true) */
  case class UserExtraRow(username: String, password: String, email: String)
  /** GetResult implicit for fetching UserExtraRow objects using plain SQL queries */
  implicit def GetResultUserExtraRow(implicit e0: GR[String]): GR[UserExtraRow] = GR{
    prs => import prs._
    UserExtraRow.tupled((<<[String], <<[String], <<[String]))
  }
  /** Table description of table user_extra. Objects of this class serve as prototypes for rows in queries. */
  class UserExtra(_tableTag: Tag) extends profile.api.Table[UserExtraRow](_tableTag, None, "user_extra") {
    def * = (username, password, email) <> (UserExtraRow.tupled, UserExtraRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(username), Rep.Some(password), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> UserExtraRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(VARCHAR), PrimaryKey, Length(191,true) */
    val username: Rep[String] = column[String]("username", O.PrimaryKey, O.Length(191,varying=true))
    /** Database column password SqlType(VARCHAR), Length(191,true) */
    val password: Rep[String] = column[String]("password", O.Length(191,varying=true))
    /** Database column email SqlType(VARCHAR), Length(191,true) */
    val email: Rep[String] = column[String]("email", O.Length(191,varying=true))

    /** Foreign key referencing User (database name fk_user_extra) */
    lazy val userFk = foreignKey("fk_user_extra", username, User)(r => r.username, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table UserExtra */
  lazy val UserExtra = new TableQuery(tag => new UserExtra(tag))
}
