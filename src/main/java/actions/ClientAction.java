package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ClientView;
import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ClientService;

/**
 * 顧客に関わる処理を行うActionクラス
 *
 */
public class ClientAction extends ActionBase {

    private ClientService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ClientService();

        // メソッドを実行
        invoke();
        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        // 管理者かどうかのチェック
        if(checkAdmin()) {

            // 指定されたページ数の一覧画面に表示するデータを取得
            int page = getPage();
            List<ClientView> clients = service.getPerPage(page);

            // 全ての顧客データの件数を取得
            long clientCount = service.countAll();

            putRequestScope(AttributeConst.CLIENTS, clients); // 取得した顧客データ
            putRequestScope(AttributeConst.CLI_COUNT, clientCount); // 全ての顧客データの件数
            putRequestScope(AttributeConst.PAGE, page); // ページ数
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); // 1ページに表示するレコードの数

            // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
            String flush = getSessionScope(AttributeConst.FLUSH);
            if(flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            // 一覧画面を表示
            forward(ForwardConst.FW_CLI_INDEX);
        }

    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOExeption
     */
    public void entryNew() throws ServletException, IOException {

        // 管理者かどうかのチェック
        if(checkAdmin()) {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
            putRequestScope(AttributeConst.CLIENT, new ClientView()); // 空の顧客インスタンス

            // 新規登録画面を表示
            forward(ForwardConst.FW_CLI_NEW);
        }
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        // CERF対策 tokenのチェック
        if(checkAdmin() && checkToken()) {

            // パラメータの値を元に顧客情報のインスタンスを作成
            ClientView cv = new ClientView(
                    null,
                    getRequestParam(AttributeConst.CLI_NUMBER),
                    getRequestParam(AttributeConst.CLI_NAME),
                    getRequestParam(AttributeConst.CLI_POST),
                    getRequestParam(AttributeConst.CLI_ADDRESS),
                    getRequestParam(AttributeConst.CLI_TEL),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());


            // 顧客情報登録
            List<String> errors = service.create(cv);

            if(errors.size() > 0) {
                // 登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
                putRequestScope(AttributeConst.CLIENT, cv); // 入力された顧客情報
                putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

                // 新規登録画面を再表示
                forward(ForwardConst.FW_CLI_NEW);

            }else {
                // 登録中にエラーがなかった場合

                // セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                // 一覧画面にリダイレクト
                redirect(ForwardConst.ACT_CLI, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        // 管理者かどうかのチェック
        if(checkAdmin()) {

            // idを条件に顧客データを取得する
            ClientView cv = service.findOne(toNumber(getRequestParam(AttributeConst.CLI_ID)));

            if(cv == null || cv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

                // データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }else {

             putRequestScope(AttributeConst.CLIENT, cv); // 取得した顧客情報

             // 詳細画面を表示
             forward(ForwardConst.FW_CLI_SHOW);
            }
        }
      }
    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        // 管理者かどうかのチェック
        if(checkAdmin()) {

            // idを条件に顧客データを取得する
            ClientView cv = service.findOne(toNumber(getRequestParam(AttributeConst.CLI_ID)));

            if(cv == null || cv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

                // データが取得できなかった、または論理削除されている場合はエラー画面を表示
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;

            }else {

                putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
                putRequestScope(AttributeConst.CLIENT, cv); // 取得した顧客情報

                // 編集画面を表示する
                forward(ForwardConst.FW_CLI_EDIT);
            }
        }
    }
    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOExcpetion
     */
    public void update() throws ServletException, IOException{

        // CSRF対策 tokenのチェック
        if(checkAdmin() && checkToken()) {
            // パラメータの値を元に顧客情報のインスタンスを作成
            ClientView cv = new ClientView(
                    toNumber(getRequestParam(AttributeConst.CLI_ID)),
                    getRequestParam(AttributeConst.CLI_NUMBER),
                    getRequestParam(AttributeConst.CLI_NAME),
                    getRequestParam(AttributeConst.CLI_POST),
                    getRequestParam(AttributeConst.CLI_ADDRESS),
                    getRequestParam(AttributeConst.CLI_TEL),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());


            // 顧客情報更新
            List<String> errors = service.update(cv);

            if(errors.size() > 0) {
               // 更新中にエラーが発生した場合

               putRequestScope(AttributeConst.TOKEN, getTokenId()); // CSRF対策用トークン
               putRequestScope(AttributeConst.CLIENT, cv); // 入力された従業員情報
               putRequestScope(AttributeConst.ERR, errors); // エラーのリスト

               // 編集画面を再表示
               forward(ForwardConst.FW_CLI_EDIT);
            }else {
                // 更新中にエラーがなかった場合

                // セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                // 一覧画面にリダイレクト
                redirect(ForwardConst.ACT_CLI, ForwardConst.CMD_INDEX);
            }


        }
    }

    /**
     * 論理削除を行う
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkAdmin() && checkToken()) {

            //idを条件に顧客データを論理削除する
            service.destroy(toNumber(getRequestParam(AttributeConst.CLI_ID)));

            //セッションに削除完了のフラッシュメッセージを設定
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面にリダイレクト
            redirect(ForwardConst.ACT_CLI, ForwardConst.CMD_INDEX);
        }
    }

    /**
     * ログイン中の従業員が管理者かどうかチェックし、管理者でなければエラー画面を表示
     * true: 管理者 false: 管理者ではない
     * @throws ServletException
     * @throws IOException
     */
    private boolean checkAdmin() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //管理者でなければエラー画面を表示
        if (ev.getAdminFlag() != AttributeConst.ROLE_ADMIN.getIntegerValue()) {

            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;

        } else {

            return true;
        }

    }
}