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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Represents relations between views used to build query.
 *
 */
public class Tree
{
    private Schema schema;
    private Node root;
    private Map<View, Node> viewNodes;
    private int index;

    /**
     * Constructor.
     * @param aSchema database schema
     */
    public Tree(Schema aSchema)
    {
        schema = aSchema;
        viewNodes = new HashMap<View, Node>();
        index = 0;
    }

    /**
     * Add new view to tree. If root is already set, method tries to find related view in tree and add to it new leaf.
     * @param aView view
     * @param aOuterJoin true, if views should be joined with 'OUTER JOIN'.
     */
    public void addJoin(View aView, boolean aOuterJoin)
    {
        if (root == null)
        {
            root = new Node(
                    new ViewAlias(aView, AliasGenerator.INSTANCE.next()), null,
                    null);
            viewNodes.put(aView, root);
        } else if (!viewNodes.containsKey(aView))
        {
            List<View> related = schema.getRelatedViews(aView);
            for (View v : related)
            {
                if (viewNodes.containsKey(v))
                {
                    ViewsRelation relation = schema
                            .getRelationOfViews(v, aView);
                    Node nn = viewNodes.get(v)
                            .addJoin(
                                    new ViewAlias(aView,
                                            AliasGenerator.INSTANCE.next()),
                                    relation, aOuterJoin);
                    viewNodes.put(aView, nn);
                    index++;
                    return;
                }
            }
            throw new IllegalArgumentException("No related views to "+aView.getName());
        }
    }
    
    /**
     * Prepare full FROM clause.
     * @return prepared string
     */
    public StringBuilder prepareString()
    {
        return root.prepareString(null);
    }
    
    /**
     * 
     * @param aView view
     * @return alias for given view saved in tree.
     */
    public String getViewAlias(View aView)
    {
        return viewNodes.get(aView).getViewAlias().getAlias();
    }
    
    public int getIndex()
    {
        return index;
    }
}
