package org.systemsbiology.uniprot;

/**
 * org.systemsbiology.uniprot.ITemplateHolder
 * User: steven
 * Date: 3/21/13
 */
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.simple.*;

/**
* org.systemsbiology.xtandem.taxonomy.ITemplateHolder
*  implemented by a class holding an JdbcTemplate -
* i.e. one that knows how to talk to a database
* User: Steve
* Date: Apr 7, 2011
*/
public interface ITemplateHolder {
    public static final ITemplateHolder[] EMPTY_ARRAY = {};

    /**
     * return the associated template
     * @return
     */
    public SimpleJdbcTemplate getTemplate();

}
