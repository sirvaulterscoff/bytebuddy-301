package test;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.Test;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

/**
 * @author pobedenniy.alexey
 * @since 11.05.17
 */
public class GenericsTest {

    public static void main(String[] args) {
        new GenericsTest().call_createView_with_generics();
    }

    @Test
    public void call_createView_with_generics() {
        Instrumentation instr = ByteBuddyAgent.install();
        new AgentBuilder.Default()
                .with(AgentBuilder.TypeStrategy.Default.REBASE)
                .type(ElementMatchers.<TypeDescription>any())
                .transform(new AgentBuilder.Transformer() {
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        System.out.println("Processing " + typeDescription.getName());

                        final ElementMatcher.Junction<MethodDescription> methodFilter = ElementMatchers.not(ElementMatchers.isHashCode().or(ElementMatchers.isEquals()).or(ElementMatchers.isClone()).or(ElementMatchers.isToString()));
                        return builder
                                .method(methodFilter)
                                .intercept(MethodDelegation.to(new Object() {
                                    public void intercept(@Origin Method method) {
                                    }
                                })
                                        .andThen(SuperMethodCall.INSTANCE));
                    }
                })
                .installOn(instr);

        new Outer().createView();
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
