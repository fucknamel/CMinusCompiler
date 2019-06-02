import java.util.List;

public class TreeNode {

    List<TreeNode> child;
    TreeNode sibling;
    int lineNum;
    Const.NodeKind nodeKind;
    Const.ExpType type;

    Const.DeclaKind decla;
    Const.StmtKind stmt;
    Const.ExpKind exp;

    Const.TokenType op;
    int val;
    String name;
}
