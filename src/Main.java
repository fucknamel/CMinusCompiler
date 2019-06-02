public class Main {

    public static void main(String[] args) {
        String sourcePath = "/Users/lkh/IdeaProjects/CMinusCompiler/src/SAMPLE.C-";
        String targetPath = "/Users/lkh/IdeaProjects/CMinusCompiler/src/SAMPLE.C--";
        Util.getFile(sourcePath);
        Util.makeFile(targetPath);

        //词法
//        Const.TokenType temp;
//        while (true){
//            temp = Scan.getToken();
//            if (temp == Const.TokenType.ENDFILE)break;
//        }

        //语法
        TreeNode root = Parse.parse();
        Util.printKind("Syntax Tree:");
        Parse.step++;
        Util.printTree(root);

        try {
            Util.fileWriter.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
