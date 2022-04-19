package constants;

/**
 *
 * 画面の項目値等を定義するEnumクラス
 *
 */

public enum AttributeConst {

    // フラッシュメッセージ
    FLUSH("flush"),

    // 一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    // 入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    // ログイン中の従業員
    LOGIN_EMP("login_employee"),

    // ログイン画面
    LOGIN_ERR("loginError"),

    // 従業員管理
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_ADMIN_FLG("admin_flag"),

    // 管理者フラグ
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    // 顧客管理
    CLIENT("client"),
    CLIENTS("clients"),
    CLI_COUNT("clients_count"),
    CLI_ID("client_id"),
    CLI_NUMBER("number"),
    CLI_NAME("name"),
    CLI_POST("post"),
    CLI_ADDRESS("address"),
    CLI_TEL("tel"),

    // 削除フラグ(従業員、顧客共通)
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    // 日報管理
    REPORT("report"),
    REPORTS("reports"),
    REP_COUNT("reports_count"),
    REP_ID("id"),
    REP_DATE("report_date"),
    REP_TITLE("title"),
    REP_PROGRESS("progress"),
    REP_CONTENT("content"),

    // 担当顧客
    SELECT_CLIENT("select_client"),

    // 進捗フラグ
    PRG_BEGIN(1),
    PRG_PLAN(2),
    PRG_POST(3),
    PRG_DEC(4),
    PRG_WAIT(5),
    PRG_END(6),
    PRG_SUC(7),
    PRG_FAIL(8),
    PRG_CANCEL(9);

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}