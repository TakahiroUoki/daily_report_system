package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 従業員情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class ClientView {

    /**
     * id
     */
    private Integer id;

    /**
     * 顧客番号
     */
    private String number;

    /**
     * 会社名(取引先名)
     */
    private String name;

    /**
     * 郵便番号
     */
    private String post;

    /**
     * 住所
     */
    private String address;

    /**
     * 電話番号
     */
    private String tel;

    /**
     *登録日時
     */
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    private LocalDateTime updatedAt;

    /**
     * 削除された顧客かどうか（現役：0、削除済み：1）
     */
    private Integer deleteFlag;

}