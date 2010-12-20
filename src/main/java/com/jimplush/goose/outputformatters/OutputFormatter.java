package com.jimplush.goose.outputformatters;

import org.jsoup.nodes.Element;

/**
 * User: jim
 * Date: 12/19/10
 */
public interface OutputFormatter {

  public Element getFormattedElement(Element topNode);


  public String getFormattedText();

}
