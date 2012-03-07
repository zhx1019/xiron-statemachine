/*  
 * Copyright 2012 xavi.ferro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.xiron.pattern.statemachine.annotated;

import junit.framework.Assert;
import net.xiron.pattern.statemachine.PhaseEnterResult;
import net.xiron.pattern.statemachine.TransitionEvent;
import net.xiron.pattern.statemachine.exceptions.StateMachineException;

import org.junit.Test;

public class LegalStateMachineTest implements AnnotatedController {
    @State @StartState public static final String STATE_A = "STATE_A";
    @State public static final String STATE_B = "STATE_B";
    @State public static final String STATE_COND = "STATE_COND";
    @State public static final String STATE_D = "STATE_D";
    
    @Event public static final String EVENT_AB = "EVENT_AB";
    @Event public static final String EVENT_BB = "EVENT_BB";
    @Event public static final String EVENT_BC = "EVENT_BC";
    @Event public static final String EVENT_CD = "EVENT_CD";
    
    @Transitions({@Transition(source=STATE_A, target=STATE_B, event=EVENT_AB),
                  @Transition(source=STATE_COND, target=STATE_D, event=EVENT_CD)})
    public void noop(TransitionEvent tEvent) {}
    
    @Transition(source=STATE_B,target=STATE_COND,event=EVENT_BC,phase=TransitionPhases.PHASE_ENTER)
    public PhaseEnterResult transitionBC(TransitionEvent tEvent) {
        return new PhaseEnterResult(EVENT_CD, null);
    }
    
    @Test
    public void test() throws StateMachineException {
        AnnotatedControllerProcessor p = new AnnotatedControllerProcessor(this);
        p.processEvent(EVENT_AB, null);
        p.processEvent(EVENT_BC, null);
        
        Assert.assertEquals(p.getStateMachine().getCurrentState(), STATE_D);
    }
}