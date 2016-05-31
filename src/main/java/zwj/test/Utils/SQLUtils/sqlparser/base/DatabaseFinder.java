package zwj.test.Utils.SQLUtils.sqlparser.base;

import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * database finder
 * Created by bjliuhongbin on 15-5-13.
 */
public class DatabaseFinder implements StatementVisitor, ExpressionVisitor, ItemsListVisitor, FromItemVisitor,
        IntoTableVisitor, OrderByVisitor, PivotVisitor, SelectItemVisitor, SelectVisitor  {

    private List<String> databases = new ArrayList<String>();

    public List<String> findDatabases(Statement statement) {
        databases = new ArrayList<String>();

        Statements states = new Statements();
        states.getStatements().add(statement);

        visit(states);

        return databases;
    }

    public void visit(BinaryExpression binaryExpression) {
        Expression left = binaryExpression.getLeftExpression();
        if (left != null) {
            left.accept(this);
        }

        Expression right = binaryExpression.getRightExpression();
        if (right != null) {
            right.accept(this);
        }
    }

    public void visit(Join join) {
        FromItem rightItem = join.getRightItem();
        rightItem.accept(this);

        Expression onExpression = join.getOnExpression();
        if (onExpression != null) {
            onExpression.accept(this);
        }

        List<Column> cols = join.getUsingColumns();
        if (cols != null && cols.size() > 0) {
            for (Column col : cols) {
                col.accept(this);
            }
        }
    }

    @Override
    public void visit(NullValue nullValue) {
    }

    @Override
    public void visit(Function function) {
        ExpressionList expressionList = function.getParameters();
        if (expressionList != null) {
            expressionList.accept(this);
        }
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        Expression expression = signedExpression.getExpression();
        expression.accept(this);
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
    }

    @Override
    public void visit(DoubleValue doubleValue) {
    }

    @Override
    public void visit(LongValue longValue) {
    }

    @Override
    public void visit(DateValue dateValue) {
    }

    @Override
    public void visit(TimeValue timeValue) {
    }

    @Override
    public void visit(TimestampValue timestampValue) {
    }

    @Override
    public void visit(Parenthesis parenthesis) {
        Expression expression = parenthesis.getExpression();
        expression.accept(this);
    }

    @Override
    public void visit(StringValue stringValue) {
    }

    @Override
    public void visit(Addition addition) {
        visit((BinaryExpression) addition);
    }

    @Override
    public void visit(Division division) {
        visit((BinaryExpression) division);
    }

    @Override
    public void visit(Multiplication multiplication) {
        visit((BinaryExpression) multiplication);
    }

    @Override
    public void visit(Subtraction subtraction) {
        visit((BinaryExpression) subtraction);
    }

    @Override
    public void visit(AndExpression andExpression) {
        visit((BinaryExpression) andExpression);
    }

    @Override
    public void visit(OrExpression orExpression) {
        visit((BinaryExpression) orExpression);
    }

    @Override
    public void visit(Between between) {
        Expression left = between.getLeftExpression();
        left.accept(this);
        Expression start = between.getBetweenExpressionStart();
        start.accept(this);
        Expression end = between.getBetweenExpressionEnd();
        end.accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        visit((BinaryExpression) equalsTo);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        visit((BinaryExpression) greaterThan);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        visit((BinaryExpression) greaterThanEquals);
    }

    @Override
    public void visit(InExpression inExpression) {
        Expression left = inExpression.getLeftExpression();
        if (left != null) {
            left.accept(this);
        }

        ItemsList leftItems = inExpression.getLeftItemsList();
        if (leftItems != null) {
            leftItems.accept(this);
        }

        ItemsList rightItems = inExpression.getRightItemsList();
        if (rightItems != null) {
            rightItems.accept(this);
        }
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        Expression left = isNullExpression.getLeftExpression();
        left.accept(this);
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        visit((BinaryExpression) likeExpression);
    }

    @Override
    public void visit(MinorThan minorThan) {
        visit((BinaryExpression) minorThan);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        visit((BinaryExpression) minorThanEquals);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        visit((BinaryExpression) notEqualsTo);
    }

    @Override
    public void visit(Column tableColumn) {
        Table table = tableColumn.getTable();
        visit(table);
    }

    @Override
    public void visit(Table tableName) {
        String schema = tableName.getSchemaName();
        if (!StringUtils.isEmpty(schema) && !databases.contains(schema)) {
            databases.add(schema.trim().toUpperCase());
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        SelectBody body = subSelect.getSelectBody();
        if (body != null) {
            body.accept(this);
        }
    }

    @Override
    public void visit(ExpressionList expressionList) {
        List<Expression> expressions = expressionList.getExpressions();
        if (expressions != null && expressions.size() > 0) {
            for (Expression expression : expressions) {
                expression.accept(this);
            }
        }
    }

    @Override
    public void visit(MultiExpressionList multiExprList) {
        List<ExpressionList> expressions = multiExprList.getExprList();
        if (expressions != null && expressions.size() > 0) {
            for (ExpressionList expression : expressions) {
                expression.accept(this);
            }
        }
    }

    @Override
    public void visit(SubJoin subJoin) {
        FromItem left = subJoin.getLeft();
        left.accept(this);

        Join join = subJoin.getJoin();
        visit(join);
    }

    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        SubSelect subSelect = lateralSubSelect.getSubSelect();

        visit(subSelect);
    }

    @Override
    public void visit(ValuesList valuesList) {
        MultiExpressionList multiExpressionList = valuesList.getMultiExpressionList();
        multiExpressionList.accept(this);
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        Expression _switch = caseExpression.getSwitchExpression();
        if (_switch != null) {
            _switch.accept(this);
        }

        List<Expression> _when = caseExpression.getWhenClauses();
        if (_when != null && _when.size() > 0) {
            for (Expression item : _when) {
                item.accept(this);
            }
        }

        Expression _else = caseExpression.getElseExpression();
        if (_else != null) {
            _else.accept(this);
        }
    }

    @Override
    public void visit(WhenClause whenClause) {
        Expression _when = whenClause.getWhenExpression();
        _when.accept(this);
        Expression _then = whenClause.getThenExpression();
        _then.accept(this);
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        Expression right = existsExpression.getRightExpression();
        right.accept(this);
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        SubSelect subSelect = allComparisonExpression.getSubSelect();
        if (subSelect != null) {
            visit(subSelect);
        }
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        SubSelect subSelect = anyComparisonExpression.getSubSelect();
        if (subSelect != null) {
            visit(subSelect);
        }
    }

    @Override
    public void visit(Concat concat) {
        visit((BinaryExpression) concat);
    }

    @Override
    public void visit(Matches matches) {
        visit((BinaryExpression) matches);
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        visit((BinaryExpression) bitwiseAnd);
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        visit((BinaryExpression) bitwiseOr);
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        visit((BinaryExpression) bitwiseXor);
    }

    @Override
    public void visit(CastExpression cast) {
        Expression left = cast.getLeftExpression();
        left.accept(this);
    }

    @Override
    public void visit(Modulo modulo) {
        visit((BinaryExpression) modulo);
    }

    @Override
    public void visit(AnalyticExpression aexpr) {
        ExpressionList partitionExpressionList = aexpr.getPartitionExpressionList();
        if (partitionExpressionList != null) {
            partitionExpressionList.accept(this);
        }

        List<OrderByElement> orderByElements = aexpr.getOrderByElements();
        if (orderByElements != null && orderByElements.size() > 0) {
            for (OrderByElement item : orderByElements) {
                item.accept(this);
            }
        }

        Expression expression = aexpr.getExpression();
        expression.accept(this);

        Expression offset = aexpr.getOffset();
        offset.accept(this);

        Expression defaultValue = aexpr.getDefaultValue();
        defaultValue.accept(this);
    }

    @Override
    public void visit(ExtractExpression eexpr) {
        Expression expression = eexpr.getExpression();
        expression.accept(this);
    }

    @Override
    public void visit(IntervalExpression iexpr) {
    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {
        Expression start = oexpr.getStartExpression();
        if (start != null) {
            start.accept(this);
        }

        Expression connect = oexpr.getConnectExpression();
        connect.accept(this);
    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {
    }

    @Override
    public void visit(JsonExpression jsonExpr) {
    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {
    }

    @Override
    public void visit(OrderByElement orderBy) {
        Expression expression = orderBy.getExpression();
        expression.accept(this);
    }

    @Override
    public void visit(Pivot pivot) {
    }

    @Override
    public void visit(PivotXml pivot) {
        SelectBody body = pivot.getInSelect();
        body.accept(this);
    }

    @Override
    public void visit(AllColumns allColumns) {
    }

    @Override
    public void visit(AllTableColumns allTableColumns) {
        Table table = allTableColumns.getTable();
        visit(table);
    }

    @Override
    public void visit(SelectExpressionItem selectExpressionItem) {
        Expression expression = selectExpressionItem.getExpression();
        expression.accept(this);
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        FromItem from = plainSelect.getFromItem();
        if (from != null) {
            from.accept(this);
        }

        List<Table> intoTables = plainSelect.getIntoTables();
        if (intoTables != null && intoTables.size() > 0) {
            for (Table table : intoTables) {
                visit(table);
            }
        }

        List<Join> joins = plainSelect.getJoins();
        if (joins != null && joins.size() > 0 ) {
            for (Join join : joins) {
                visit(join);
            }
        }

        Expression where = plainSelect.getWhere();
        if (where != null) {
            where.accept(this);
        }

        Expression having = plainSelect.getHaving();
        if (having != null) {
            having.accept(this);
        }

        OracleHierarchicalExpression oracleHierarchicalExpression = plainSelect.getOracleHierarchical();
        if (oracleHierarchicalExpression != null) {
            oracleHierarchicalExpression.accept(this);
        }
    }

    @Override
    public void visit(SetOperationList setOpList) {
        List<PlainSelect> selects = setOpList.getPlainSelects();
        if (selects != null && selects.size() > 0 ) {
            for (PlainSelect select : selects) {
                select.accept(this);
            }
        }

        List<OrderByElement> orderByElements = setOpList.getOrderByElements();
        if (orderByElements != null && orderByElements.size() > 0 ) {
            for (OrderByElement orderByElement : orderByElements) {
                orderByElement.accept(this);
            }
        }
    }

    @Override
    public void visit(WithItem withItem) {
        SelectBody body = withItem.getSelectBody();
        if (body != null) {
            body.accept(this);
        }
    }

    @Override
    public void visit(Select select) {
        SelectBody body = select.getSelectBody();
        body.accept(this);

        List<WithItem> withItems = select.getWithItemsList();
        if (withItems != null && withItems.size() > 0) {
            for (WithItem item : withItems) {
                item.accept(this);
            }
        }
    }

    @Override
    public void visit(Delete delete) {
        Table table = delete.getTable();
        visit(table);

        Expression where = delete.getWhere();
        if (where != null) {
            where.accept(this);
        }
    }

    @Override
    public void visit(Update update) {
        List<Table> tables = update.getTables();
        for (Table table : tables) {
            visit(table);
        }

        Expression where  = update.getWhere();
        if (where != null) {
            where.accept(this);
        }

        FromItem from = update.getFromItem();
        if (from != null) {
            from.accept(this);
        }

        List<Join> joins = update.getJoins();
        if (joins != null && joins.size() > 0) {
            for (Join join : joins) {
                visit(join);
            }
        }
    }

    @Override
    public void visit(Insert insert) {
        Table table = insert.getTable();
        if (table != null) {
            visit(table);
        }

        ItemsList itemsList = insert.getItemsList();
        if (itemsList != null) {
            itemsList.accept(this);
        }

        Select select = insert.getSelect();
        if (select != null) {
            select.accept(this);
        }

        List<SelectExpressionItem> selectExpressionItemList = insert.getReturningExpressionList();
        if (selectExpressionItemList != null && selectExpressionItemList.size() > 0) {
            for (SelectExpressionItem item : selectExpressionItemList) {
                item.accept(this);
            }
        }
    }

    @Override
    public void visit(Replace replace) {
        Table table = replace.getTable();
        visit(table);

        ItemsList itemsList = replace.getItemsList();
        if (itemsList != null) {
            itemsList.accept(this);
        }

        List<Expression> expressions = replace.getExpressions();
        if (expressions != null && expressions.size() > 0) {
            for (Expression expression : expressions) {
                expression.accept(this);
            }
        }
    }

    @Override
    public void visit(Drop drop) {
        String type = drop.getType();
        if ("database".equalsIgnoreCase(type)) {
            String name = drop.getName().toUpperCase();
            if (!StringUtils.isEmpty(name) && !this.databases.contains(name)) {
                this.databases.add(name.trim().toUpperCase());
            }
        }
    }

    @Override
    public void visit(Truncate truncate) {
        Table table = truncate.getTable();
        visit(table);
    }

    @Override
    public void visit(CreateIndex createIndex) {
        Table table = createIndex.getTable();
        visit(table);
    }

    @Override
    public void visit(CreateTable createTable) {
        Table table = createTable.getTable();
        visit(table);

        Select select = createTable.getSelect();
        if (select != null) {
            select.accept(this);
        }
    }

    @Override
    public void visit(CreateView createView) {
        Table view = createView.getView();
        visit(view);

        SelectBody body = createView.getSelectBody();
        if (body != null) {
            body.accept(this);
        }
    }

    @Override
    public void visit(Alter alter) {
        Table table = alter.getTable();
        visit(table);
    }

    @Override
    public void visit(Statements stmts) {
        List<Statement> statements = stmts.getStatements();
        if (statements != null && statements.size() > 0) {
            for (Statement statement : statements) {
                statement.accept(this);
            }
        }
    }

    @Override
    public void visit(Execute execute) {
        ExpressionList expressionList = execute.getExprList();
        if (expressionList != null) {
            expressionList.accept(this);
        }
    }
}
