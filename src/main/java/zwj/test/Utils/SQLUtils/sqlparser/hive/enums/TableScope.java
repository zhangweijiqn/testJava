package zwj.test.Utils.SQLUtils.sqlparser.hive.enums;

/**
 * 表的作用域
 * @author bjliuhongbin
 *
 */
public enum TableScope {

//	FROM,// 来至from
//	INSERT, // 来至INSERT OVERWRITE/INSERT INTO
//	JOIN,// 来至JOIN
//	CREATE,// 来至create
//	DROP,// 来至drop
//	TRUNCATE,// 来至TRUNCATE
//	ALERT// 来至alert
    CREATE_TABLE,
    CREATE_TABLE_LIKE,
    CREATE_INDEX,
    DROP_TABLE,
    TRUNCATE_TABLE,
    ALTER_TABLE,
    SHOW_TABLE_ABOUT,
    DESC_TABLE,
    LOAD_TABLE,
    QUERY_TABLE,
    JOIN_TABLE,
    INSERT_TABLE,
    UPDATE_TABLE,
    REPLACE_TABLE,
    DELETE_TABLE,
    INTO_TABLE
}
