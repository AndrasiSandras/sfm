package unideb.hu.SFMProject;

public class Utils {

    private DAO pDAO;


    public Utils(DAO pDAO) {
        this.pDAO = pDAO;

    }


    public void runBUtils(){
        Beszallito beszallito = new Beszallito();

        /* SceneBuilder kiolvasás implementácio */
        beszallito.setName("Lajos");
        beszallito.setEmail("Lajos@valami.hu");
        beszallito.setAddress("Lajos utca 56");
        beszallito.setPassword("1234");

        pDAO.saveBeszallito(beszallito);

    }

    public void runPUtils(Product product)
    {

        pDAO.saveProduct(product);

    }

    public void runCUtils(RegLogin c)
    {
        pDAO.saveRegLog(c);
    }
}
