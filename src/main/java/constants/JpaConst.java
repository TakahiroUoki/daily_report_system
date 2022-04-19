package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数はpublic static final 修飾子がついているとみなされる
 */

public interface JpaConst {

    // persistence-unit名
    String PERSISTENCE_UNIT_NAME = "daily_report_system";

    // データ取得件数の最大値
    int ROW_PER_PAGE = 15; // 1ページに表示するレコードの数

    // 従業員テーブル
    String TABLE_EMP = "employees"; // テーブル名
    // 従業員テーブルカラム
    String EMP_COL_ID = "id"; // id
    String EMP_COL_CODE = "code"; // 社員番号
    String EMP_COL_NAME = "name"; // 氏名
    String EMP_COL_PASS = "password"; // パスワード
    String EMP_COL_ADMIN_FLAG = "admin_flag"; // 管理者権限
    String EMP_COL_CREATED_AT = "created_at"; // 登録日時
    String EMP_COL_UPDATED_AT = "updated_at"; // 更新日時
    String EMP_COL_DELETE_FLAG = "delete_flag"; // 削除フラグ

    int ROLE_ADMIN = 1; // 管理者権限ON(管理者)
    int ROLE_GENERAL = 0; // 管理者権限OFF(一般)
    int EMP_DEL_TRUE = 1; // 削除フラグON(削除済み)
    int EMP_DEL_FALSE = 0; // 削除フラグOFF(現役)

    // 顧客テーブル
    String TABLE_CLI = "clients"; // テーブル名
    // 顧客テーブルカラム
    String CLI_COL_ID = "id"; // id
    String CLI_COL_NUMBER = "number"; // 顧客番号
    String CLI_COL_NAME = "name"; // 会社名
    String CLI_COL_POST = "post"; // 郵便番号
    String CLI_COL_ADDRESS = "address"; // 住所
    String CLI_COL_TEL = "tel"; // 電話番号
    String CLI_COL_CREATED_AT = "created_at"; // 登録日時
    String CLI_COL_UPDATED_AT = "updated_at"; // 更新日時
    String CLI_COL_DELETE_FLAG = "delete_flag"; // 削除フラグ

    int CLI_DEL_TRUE = 1; // 削除フラグON(削除済み)
    int CLI_DEL_FALSE = 0; // 削除フラグOFF(現役)

    // 日報テーブル
    String TABLE_REP = "reports"; // テーブル名
    // 日報テーブルカラム
    String REP_COL_ID = "id"; // id
    String REP_COL_EMP = "employee_id"; // 日報を作成した従業員のid
    String REP_COL_REP_DATE = "report_date"; // いつの日報かを示す日付
    String REP_COL_TITLE = "title"; // 日報のタイトル
    String REP_COL_CLI = "client_id"; // 担当顧客のid
    String REP_COL_PROGRESS = "progress"; //進捗
    String REP_COL_CONTENT = "content"; // 日報の内容
    String REP_COL_CREATED_AT = "created_at"; // 登録日時
    String REP_COL_UPDATED_AT = "updated_at"; // 更新日時

    int REP_PRG_BEGIN = 1; // 開始前
    int REP_PRG_PLAN = 2; // 打合せ予定
    int REP_PRG_POST = 3; // 打合せ延期
    int REP_PRG_DEC = 4; // 打合せ確定
    int REP_PRG_WAIT = 5; // 結果待ち
    int REP_PRG_END = 6; // 終了(交渉可否無関係)
    int REP_PRG_SUC = 7; // 終了(交渉成功)
    int REP_PRG_FAIL = 8; // 終了(交渉失敗)
    int REP_PRG_CANCEL = 9; // キャンセル

    // Entity名
    String ENTITY_EMP = "employee"; // 従業員
    String ENTITY_CLI = "client"; // 顧客
    String ENTITY_REP = "report"; // 日報

    // JPQL内パラメータ
    String JPQL_PARM_CODE = "code"; // 社員番号
    String JPQL_PARM_NUMBER = "number"; // 顧客番号
    String JPQL_PARM_PASSWORD = "password"; // パスワード
    String JPQL_PARM_EMPLOYEE = "employee"; // 従業員
    String JPQL_PARM_CLIENT = "client"; // 顧客
    String JPQL_PARM_PROGRESS = "progress"; // 進捗

    // NamedQueryのnameとquery
    // 全ての従業員をidの降順に取得する
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; // name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    // 全ての従業員の件数を取得する
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    // 社員番号とハッシュ化済パスワードを条件に未削除の従業員を取得する
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //指定した社員番号を保持する従業員の件数を取得する
    String Q_EMP_COUNT_RESISTERED_BY_CODE = ENTITY_EMP + ".countRegisteredByCode";
    String Q_EMP_COUNT_RESISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    // すべての顧客をidの降順に取得する
    String Q_CLI_GET_ALL = ENTITY_CLI + ".getAll"; // name
    String Q_CLI_GET_ALL_DEF = "SELECT c FROM Client AS c ORDER BY c.id DESC"; //query
    // 全ての顧客の件数を取得する
    String Q_CLI_COUNT = ENTITY_CLI + ".count";
    String Q_CLI_COUNT_DEF = "SELECT COUNT(c) FROM Client AS c";
    // 顧客番号を条件に未削除の顧客を取得する
    String Q_CLI_GET_BY_NUMBER = ENTITY_CLI + ".getByNumber";
    String Q_CLI_GET_BY_NUMBER_DEF = "SELECT c FROM Client AS c WHERE c.deleteFlag = 0 AND c.number = :" + JPQL_PARM_NUMBER;
    //指定した顧客番号を保持する顧客の件数を取得する
    String Q_CLI_COUNT_RESISTERED_BY_NUMBER = ENTITY_CLI + ".countRegisteredByNumber";
    String Q_CLI_COUNT_RESISTERED_BY_NUMBER_DEF = "SELECT COUNT(c) FROM Client AS c WHERE c.number = :" + JPQL_PARM_NUMBER;
    // 未削除の顧客IDと顧客名を取得する
    String Q_CLI_GET_NAMELIST = ENTITY_CLI + ".getByNamelist";
    String Q_CLI_GET_NAMELIST_DEF = "SELECT c FROM Client AS c WHERE c.deleteFlag = 0 ORDER BY c.id ASC";
    //全ての日報をidの降順に取得する
    String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
    //全ての日報の件数を取得する
    String Q_REP_COUNT = ENTITY_REP + ".count";
    String Q_REP_COUNT_DEF = "SELECT COUNT(r) FROM Report AS r";
    // 指定した進捗の日報を取得する
    String Q_REP_GET_BY_PROGRESS = ENTITY_REP + ".getByProgress";
    String Q_REP_GET_BY_PROGRESS_DEF = "SELECT r FROM Report AS r WHERE r.progress = :" + JPQL_PARM_PROGRESS;
    // 指定した進捗の日報の件数を全て取得する
    String Q_REP_COUNT_RESISTERED_BY_PROGRESS = ENTITY_REP + ".countResisteredByProgress";
    String Q_REP_COUNT_RESISTERED_BY_PROGRESS_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.progress = :" + JPQL_PARM_PROGRESS;
    //指定した従業員が作成した日報を取得する
    String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE ;
    //指定した従業員が作成した日報の件数を取得する
    String Q_REP_COUNT_ALL_MINE = ENTITY_REP + ".countAllMine";
    String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;
}
