package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ClientView;
import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ClientService;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class ReportAction extends ActionBase {

    private ReportService service;
    private ClientService service2;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();
        service2 = new ClientService();

        // メソッドを実行
        invoke();
        service.close();
        service2.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOexception
     */
    public void index() throws ServletException, IOException {

        // 指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        // 全日報データの件数を取得
        long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports);  // 取得した日報データ
        putRequestScope(AttributeConst.REP_COUNT, reportsCount); // すべての日報データの件数
        putRequestScope(AttributeConst.PAGE, page); // ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数

        // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        // 一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン

        // 日報情報の空インスタンスに、日報の日付=今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv); // 日付のみ設定済みの日報インスタンス

        // 新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        // CSRF対策 tokenのチェック
        if(checkToken()) {

            // 日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if(getRequestParam(AttributeConst.REP_DATE) == null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")) {
                day = LocalDate.now();
            }else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            // セッションからログイン中の従業員情報を取得
            EmployeeView ev= (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            // 顧客idを条件に顧客リストを取得する
            ClientView cv = service2.findOne(toNumber(getRequestParam(AttributeConst.CLI_ID)));

            // パラメータの値をもとに日報情報のインスタンスを作成する
            ReportView rv = new ReportView(
                    null,
                    ev, // ログインしている従業員を、日報作成者として登録する
                    day,
                    getRequestParam(AttributeConst.REP_TITLE),
                    cv,
                    toNumber(getRequestParam(AttributeConst.REP_PROGRESS)),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null);

            // 日報情報登録
            List<String> errors = service.create(rv);

            if(errors.size() > 0) {
                // 登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); // 入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

                // 新規登録画面を再表示
                forward(ForwardConst.FW_REP_NEW);

            }else{
                // 登録中にエラーがなかった場合

                // セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                // 一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        // idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if(rv == null) {
            // 該当の日報データまたは担当顧客が存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        }else {
            putRequestScope(AttributeConst.REPORT, rv); // 取得した日報データ

            // 詳細画面を表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        // idを条件に日報データを取得する
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        // セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        // 顧客idを条件に顧客リストを取得する
        ClientView cv = service2.findOne(toNumber(getRequestParam(AttributeConst.CLI_ID)));

        if(rv == null || ev.getId() != rv.getEmployee().getId() || cv == null) {
            // 該当の日報データが存在しない、ログインしている従業員が日報の作成者でない
            // または担当顧客が存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        }else {

            putRequestScope(AttributeConst.CLIENT, cv); // 取得した顧客情報
            putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv); // 取得した日報データ

            // 編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        // CSRF対策 tokenのチェック
        if(checkToken()) {

            // idを条件に日報データを取得する
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            // 顧客idを条件に顧客リストを取得する
            ClientView cv = service2.findOne(toNumber(getRequestParam(AttributeConst.CLI_ID)));

            // 入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setClient(cv);
            rv.setProgress(toNumber(getRequestParam(AttributeConst.REP_PROGRESS)));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            // 日報データを更新する
            List<String> errors = service.update(rv);

            if(errors.size() > 0) {
                // 更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); // 入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

                // 編集画面を再表示
                forward(ForwardConst.FW_REP_EDIT);
            }else {
                // 更新中にエラーがなかった場合

                // セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                // 一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }

    /**
     * 入力された進捗の一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void search() throws ServletException, IOException {

              //指定されたページ数の一覧画面に表示する入力された進捗の日報データを取得
              int page = getPage();
              Integer progress = toNumber(request.getParameter(AttributeConst.REP_PROGRESS.getValue()));

              // 進捗を条件に日報データを取得する
               List<ReportView> reports = service.getByProgressPerPage(progress,page);

              // 指定進捗データの件数を取得
              long reportsCount = service.countResisteredByProgress(progress);

              putRequestScope(AttributeConst.REPORTS, reports); // 取得した進捗の日報データ
              putRequestScope(AttributeConst.REP_COUNT, reportsCount); // 指定した進捗の日報件数
              putRequestScope(AttributeConst.PAGE, page); //ページ数
              putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

              // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
              String flush = getSessionScope(AttributeConst.FLUSH);
              if(flush != null) {
                 putRequestScope(AttributeConst.FLUSH, flush);
                 removeSessionScope(AttributeConst.FLUSH);
              }

        //選択画面を表示
        forward(ForwardConst.FW_REP_SEARCH);
    }
}