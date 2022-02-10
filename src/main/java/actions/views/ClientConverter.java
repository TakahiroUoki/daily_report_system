package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
import models.Client;

/**
 * 顧客データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class ClientConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param cv ClientViewのインスタンス
     * @return Clientのインスタンス
     */
    public static Client toModel(ClientView cv) {

        return new Client(
                cv.getId(),
                cv.getNumber(),
                cv.getName(),
                cv.getPost(),
                cv.getAddress(),
                cv.getTel(),
                cv.getCreatedAt(),
                cv.getUpdatedAt(),
                cv.getDeleteFlag() == null
                    ? null
                    : cv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                        ? JpaConst.CLI_DEL_TRUE
                        : JpaConst.CLI_DEL_FALSE);
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param c Clientのインスタンス
     * @return ClientViewのインスタンス
     */
    public static ClientView toView(Client c) {

        if(c == null) {
            return null;
        }

        return new ClientView(
                c.getId(),
                c.getNumber(),
                c.getName(),
                c.getPost(),
                c.getAddress(),
                c.getTel(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getDeleteFlag() == null
                    ? null
                    : c.getDeleteFlag() == JpaConst.CLI_DEL_TRUE
                        ? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                        : AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param List DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<ClientView> toViewList(List<Client> list) {
        List<ClientView> cvs = new ArrayList<>();

        for(Client c : list) {
            cvs.add(toView(c));
        }

        return cvs;

    }

    /**
     * viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param c DTOモデル(コピー先)
     * @param cv Viewモデル(コピー元)
     */
    public static void copyViewToModel(Client c, ClientView cv) {
        c.setId(cv.getId());
        c.setNumber(cv.getNumber());
        c.setName(cv.getName());
        c.setPost(cv.getPost());
        c.setAddress(cv.getAddress());
        c.setTel(cv.getTel());
        c.setCreatedAt(cv.getCreatedAt());
        c.setUpdatedAt(cv.getUpdatedAt());
        c.setDeleteFlag(cv.getDeleteFlag());
    }

}
