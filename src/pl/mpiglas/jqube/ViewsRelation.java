/*
 * Select query builder
 * Copyright (C) 2014  Milosz Piglas [milosz@archeocs.com]
 *    
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pl.mpiglas.jqube;

import java.util.List;

/**
 * Represents relation between two views in database's schema.
 * 
 */
public class ViewsRelation
{
    private List<AttributesPair> attrPairs;

    /**
     * Inits relation. Each pair of views migth have common one or more attributes.
     * @param aPairs list of pairs of common attributes, that define relation between views.
     */
    public ViewsRelation(List<AttributesPair> aPairs)
    {
        attrPairs = aPairs;
    }

    /**
     * Prepares string for query, which represents relation between two views.
     * @param aAlias view's alias
     * @param aAlias2 view's alias
     * @return prepared string
     */
    public StringBuilder prepareString(ViewAlias aAlias, ViewAlias aAlias2)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(attrPairs.get(0).prepareString(aAlias, aAlias2));
        if (attrPairs.size() > 1)
        {
            for (int i = 1; i < attrPairs.size(); i++)
            {
                builder.append(" AND ").append(attrPairs.get(i).prepareString(aAlias, aAlias2));
            }
        }
        return builder;
    }
    
    /**
     * 
     * @param aView view
     * @return true if this relation represents relation of given view and other one.
     */
    public boolean isViewRelation(View aView)
    {
        return attrPairs.get(0).containsViewAttribute(aView);
    }

    /**
     * 
     * @param aOther view
     * @return view related to given one.
     */
    public View getRelatedView(View aOther)
    {
        return attrPairs.get(0).getRelatedAttribute(aOther).getView();
    }
}
