
/**
 * @author pobedenniy.alexey
 * @since 11.05.17
 */
public class Generics {

    public static void main(String[] args) {
        new Outer().createView();
        System.out.println("All ok");
    }


    public static class Outer extends Inner<InnerView> {
        @Override
        protected View innerCreateView() {
            return new InnerView() {
            };
        }
    }

    public static class Inner<T extends InnerView> extends Abstract {

        @Override
        protected View innerCreateView() {
            return new InnerView() {
            };
        }
    }

    public static abstract class Abstract<T extends View> extends Base<T> {

    }

    public static abstract class Base<T extends Component> {
        public T createView() {
            return innerCreateView();
        }

        protected abstract T innerCreateView();
    }

    public interface Component {

    }

    public interface View extends Component {
    }

    public interface InnerView extends View {
    }

}
