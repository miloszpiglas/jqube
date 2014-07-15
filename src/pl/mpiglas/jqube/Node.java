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

import java.util.ArrayList;
import java.util.List;

/**
 * Node of tree used to define relation of views used in {@link QueryBuilder}
 * 
 */
public class Node
{
    private ViewAlias alias;
    private ViewsRelation relation;
    private String joinString;
    private List<Node> children;

    /**
     * Inits new node of tree representing single view and its relation to other
     * view.
     * 
     * @param aViewAlias
     *            view's alias.
     * @param aRelation
     *            relation of this node's view to other view. (possiblly null).
     * @param aJoinString
     *            JOIN string used to build query
     */
    public Node(ViewAlias aViewAlias, ViewsRelation aRelation,
            String aJoinString)
    {
        alias = aViewAlias;
        relation = aRelation;
        joinString = aJoinString;
        children = new ArrayList<Node>();
    }

    /**
     * Inits new node of tree that does not have defined relation to any other
     * view.
     * 
     * @param aViewAlias
     *            view's alias
     */
    public Node(ViewAlias aViewAlias)
    {
        this(aViewAlias, null, "JOIN");
    }

    public String getJoinString()
    {
        return joinString;
    }

    /**
     * Creates new node and adds it to this node as related.
     * 
     * @param aViewAlias
     *            view's alias.
     * @param aRelation
     *            relation between view from argument and view from this node.
     * @param aOuterJoin
     *            JOIN string used to build query.
     * @return new node.
     */
    public Node addJoin(ViewAlias aViewAlias, ViewsRelation aRelation,
            boolean aOuterJoin)
    {
        String joinStr = aOuterJoin ? "LEFT OUTER JOIN" : "JOIN";
        Node nn = new Node(aViewAlias, aRelation, joinStr);
        children.add(nn);
        return nn;
    }

    /**
     * Builds part of FROM clause for this node and all its children.
     * 
     * @param aParentAlias
     *            alias of parent view. Null if this node is not connected with
     *            other node (root).
     * @return prepared string builder.
     */
    public StringBuilder prepareString(ViewAlias aParentAlias)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(alias.getView().getSource()).append(" ")
                .append(alias.getAlias());
        if (relation != null)
        {
            builder.append(" ON ").append(
                    relation.prepareString(aParentAlias, alias));
        }
        for (Node ch : children)
        {
            builder.append("\n").append(ch.getJoinString()).append(" ")
                    .append(ch.prepareString(alias));
        }
        return builder;
    }

    public ViewAlias getViewAlias()
    {
        return alias;
    }
}
