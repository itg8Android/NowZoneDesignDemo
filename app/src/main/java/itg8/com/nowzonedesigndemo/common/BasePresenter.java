package itg8.com.nowzonedesigndemo.common;

/**
 * Created by itg_Android on 6/10/2017.
 */

public class BasePresenter{

    private Object object;

    public BasePresenter(Object object) {

        this.object = object;
    }

    public boolean isNotNull(){
        return object!=null;
    }

    public void setNull(){
        object=null;
    }
}
