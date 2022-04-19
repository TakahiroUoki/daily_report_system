package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.ClientConverter;
import actions.views.ClientView;
import constants.JpaConst;
import models.Client;
import models.validators.ClientValidator;

/**
 * 顧客テーブルの操作に関わる処理を行うクラス
 */
public class ClientService  extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、ClientViewのリストで返却
     * @param page ページ数
     * @return 表示するデータのリスト
     */
    public List<ClientView> getPerPage(int page){
        List<Client> clients = em.createNamedQuery(JpaConst.Q_CLI_GET_ALL, Client.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return ClientConverter.toViewList(clients);
    }

    /**
     * 顧客テーブルのデータの件数を取得し、返却する
     * @return 顧客テーブルのデータの件数
     */
    public long countAll() {
        long cliCount = (long) em.createNamedQuery(JpaConst.Q_CLI_COUNT, Long.class)
                .getSingleResult();

        return cliCount;
    }

    /**
     * 顧客番号を条件に取得したデータをClientViewのインスタンスで返却する
     * @param number 顧客番号
     * @return 取得データのインスタンス 取得できない場合null
     */
    public ClientView findOne(String number) {
        Client c = null;
        try {

            // 顧客番号を条件に未削除の顧客を1件取得する
            c = em.createNamedQuery(JpaConst.Q_CLI_GET_BY_NUMBER, Client.class)
                    .setParameter(JpaConst.JPQL_PARM_NUMBER, number)
                    .getSingleResult();
        }catch (NoResultException ex) {
        }

        return ClientConverter.toView(c);
    }

    /**
     * clientIdを条件に取得したデータをClientViewのインスタンスで返却する
     * @param clientId
     * @return 取得データのインスタンス
     */
    public ClientView findOne(int clientId) {
        Client c = findOneInternal(clientId);
        return ClientConverter.toView(c);
    }

    /**
     * 顧客番号を条件に該当するデータの件数を取得し、返却する
     * @param number 顧客番号
     * @return 該当するデータの件数
     */
    public long countByNumber(String number) {

        //指定した顧客番号を保持する顧客の件数を取得する
        long clients_count = (long) em.createNamedQuery(JpaConst.Q_CLI_COUNT_RESISTERED_BY_NUMBER, Long.class)
                .setParameter(JpaConst.JPQL_PARM_NUMBER, number)
                .getSingleResult();
        return clients_count;
    }

    /**
     * 画面から入力された顧客の登録内容をもとにデータを1件作成し、顧客テーブルに登録する
     * @param cv 画面から入力された顧客の登録内容
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
    public List<String> create(ClientView cv) {

        // 登録日時、更新日時は現在時刻を設定する
        LocalDateTime now = LocalDateTime.now();
        cv.setCreatedAt(now);
        cv.setUpdatedAt(now);

        // 登録内容のバリデーションを行う
        List<String> errors = ClientValidator.validate(this, cv, true);

        // バリデーションエラーがなければデータを登録する
        if(errors.size() == 0) {
            createInternal(cv);
        }

        // エラーを返却(エラーがなければ0件の空リスト)
        return errors;
    }

    /**
     * 画面から入力された顧客の更新内容を元にデータを1件作成し、顧客テーブルを更新する
     * @param cv 画面から入力された顧客の登録内容
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(ClientView cv) {

        //idを条件に登録済みの従業員情報を取得する
       ClientView savedCli = findOne(cv.getClientId());

       boolean validateNumber = false;
       if(!savedCli.getNumber().equals(cv.getNumber())) {
           // 顧客番号を更新する場合

           // 顧客番号についてのバリデーションを行う
           validateNumber = true;
           // 変更後の顧客番号を設定する
           savedCli.setNumber(cv.getNumber());
       }

        savedCli.setName(cv.getName()); //変更後の顧客名(取引先名)を設定する
        savedCli.setPost(cv.getPost()); //変更後の郵便番号を設定する
        savedCli.setAddress(cv.getAddress()); // 変更後の住所を設定する
        savedCli.setTel(cv.getTel()); //変更後の電話番号を設定する

        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedCli.setUpdatedAt(today);

        //更新内容についてバリデーションを行う
        List<String> errors = ClientValidator.validate(this, savedCli, validateNumber);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {
            updateInternal(savedCli);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * clientIdを条件に顧客データを論理削除する
     * @param clientId
     */
    public void destroy(Integer clientId) {

        // idを条件に登録済みの顧客情報を取得する
        ClientView savedCli = findOne(clientId);

        // 更新日時に現在時刻を設定
        LocalDateTime today = LocalDateTime.now();
        savedCli.setUpdatedAt(today);

        // 論理削除フラグをたてる
        savedCli.setDeleteFlag(JpaConst.CLI_DEL_TRUE);

        // 更新処理を行う
        updateInternal(savedCli);

    }

    /**
     * 顧客番号を条件に検索し、データが取得できるかどうかで認証結果を返却
     * @param number 顧客番号
     * @return 認証結果を返却(成功:true 失敗:false)
     */
    public Boolean validateLogin(String number) {

        boolean isValidClient = false;
        if(number != null && !number.equals("")) {
            ClientView cv = findOne(number);

            if(cv != null && cv.getClientId() != null) {

                // データが取得できた場合、認証成功
                isValidClient = true;
            }
        }

       // 認証結果を返却
        return isValidClient;
    }

    /**
     * clientIdを条件にデータを1件取得し、Clientのインスタンスで返却する
     * @param clientId
     * @return 取得データのインスタンス
     */
    private Client findOneInternal(int clientId) {
        Client c = em.find(Client.class, clientId);

        return c;
    }

    /**
     * 顧客データを1件登録する
     * @param cv 顧客データ
     * @return 登録結果(成功:true 失敗:false)
     */
    private void createInternal(ClientView cv) {

        em.getTransaction().begin();
        em.persist(ClientConverter.toModel(cv));
        em.getTransaction().commit();

    }

    /**
     * 顧客データを更新する
     * @param cv 画面から入力された顧客の登録内容
     */
    private void updateInternal(ClientView cv) {

        em.getTransaction().begin();
        Client c = findOneInternal(cv.getClientId());
        ClientConverter.copyViewToModel(c, cv);
        em.getTransaction().commit();

    }

    /**
     * 削除されていないすべての顧客名と顧客IDを取得し、ClientViewのリストで返却
     * @return 表示するデータのリスト
     */
    public List<ClientView> getByNameList(){

        List<Client> clients = em.createNamedQuery(JpaConst.Q_CLI_GET_NAMELIST, Client.class)
                .getResultList();

        return ClientConverter.toViewList(clients);
    }

}