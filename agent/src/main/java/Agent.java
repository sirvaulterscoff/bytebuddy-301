import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * @author pobedenniy.alexey
 * @since 12.05.17
 */
public class Agent {
    public static void premain(String agentArgs, final Instrumentation inst) {
        new AgentBuilder.Default()
                .with(AgentBuilder.TypeStrategy.Default.REBASE)
                .type(ElementMatchers.<TypeDescription>any())
                .transform(new AgentBuilder.Transformer() {
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        System.out.println("Processing " + typeDescription.getName());

                        final ElementMatcher.Junction<MethodDescription> methodFilter = ElementMatchers.not(ElementMatchers.isHashCode().or(ElementMatchers.isEquals()).or(ElementMatchers.isClone()).or(ElementMatchers.isToString()));
                        return builder
                                .method(methodFilter)
                                .intercept(MethodDelegation.to(UsageInterceptor.class)
                                        .andThen(SuperMethodCall.INSTANCE));
                    }
                })
                .installOn(inst);
    }
}
