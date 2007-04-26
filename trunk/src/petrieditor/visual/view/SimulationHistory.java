package petrieditor.visual.view;

import petrieditor.model.Transition;
import petrieditor.model.event.EventType;
import petrieditor.model.event.NotifyEvent;
import petrieditor.model.viewinterfaces.TransitionView;
import petrieditor.util.Observable;

import javax.swing.*;
import java.awt.*;

/**
 * @author wiktor
 */
public class SimulationHistory extends JLabel implements TransitionView {

    private StringBuilder sb;
    private int counter;

    public SimulationHistory() {
        _resetText();
        setVerticalAlignment(SwingConstants.TOP);
        setFocusable(false);
        setFont(new Font("Verdana", Font.BOLD, 10));
        setForeground(new Color(0x929292));
    }

    private void _resetText() {
        counter = 1;
        sb = new StringBuilder();
        sb.append("<html><font color='red'>&nbsp;Fired transitions:</font><br></html>");
        setText(sb.toString());
    }

    public void setVisible(boolean aFlag) {
        if (aFlag)
            _resetText();
        else
            sb = null;
        super.setVisible(aFlag);
    }

    public void update(Observable<Transition, TransitionView, NotifyEvent<Transition>> observable, NotifyEvent<Transition> event) {
        if (event == null || event.getEventType() != EventType.TRANSITION_FIRED)
            return;

        sb.delete(sb.length() - 7, sb.length());
        sb.append("&nbsp;&nbsp;").append(counter++).append(": ").append(event.getObject().getName());
        sb.append("<br></html>");
        setText(sb.toString());
    }
}
