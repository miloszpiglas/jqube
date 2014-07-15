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
 * Representation of conditions for single attribute used to build WHERE clause.
 * One instance of class might be assigned only to one attribute.
 */
public class Condition
{
    /**
     * 
     * Supported logical functions.
     */
    public static enum Function
    {

        OR("OR"), AND("AND"), NOT("NOT");

        private String name;

        Function(String aName)
        {
            name = aName;
        }

        public String toString()
        {
            return name;
        }

    }

    /**
     * 
     * Supported SQL operators.
     * 
     */
    public static enum Operator
    {
        LT("<"), LE("<="), EQ("="), GE(">="), GT(">"), LIKE("LIKE");

        String symbol;

        Operator(String aSymbol)
        {
            symbol = aSymbol;
        }

        public String toString()
        {
            return symbol;
        }
    }

    private Function function;
    private Operator operator;
    private Condition next = null;
    private int index;

    /**
     * Creates single condition.
     * 
     * @param aLogicalFunction
     *            logical function (OR, AND, ...)
     * @param aOperator
     *            comparision operator (<, >, LIKE, ...)
     */
    public Condition(Function aLogicalFunction, Operator aOperator)
    {
        function = aLogicalFunction;
        operator = aOperator;
    }

    /**
     * @return next condition in chain.
     */
    public Condition getNext()
    {
        return next;
    }

    /**
     * @param next
     *            next condition to chain
     */
    public void setNext(Condition next)
    {
        this.next = next;
    }

    /**
     * Creates new conditon from arguments and adds to chain.
     * 
     * @param aLogicalFunction
     *            logical function
     * @param aOperator
     *            comparision SQL operator
     * @return new condition
     */
    private Condition setNext(Function aLogicalFunction, Operator aOperator)
    {
        if (next != null)
        {
            throw new IllegalArgumentException(
                    "Next condition in chain is already defined - use setNext instead");
        }
        next = new Condition(aLogicalFunction, aOperator);
        return next;
    }

    /**
     * Creates new condition with logical function AND and adds to chain.
     * 
     * @param aOperator
     *            comparision SQL operator
     * @return new condition.
     */
    public Condition and(Operator aOperator)
    {
        return setNext(Function.AND, aOperator);
    }

    /**
     * Creates new condition with logical function OR and adds to chain.
     * 
     * @param aOperator
     *            comparision SQL operator
     * @return new condition.
     */
    public Condition or(Operator aOperator)
    {
        return setNext(Function.OR, aOperator);
    }

    /**
     * Creates new condition with logical function NOT and adds to chain.
     * 
     * @param aOperator
     *            comparision SQL operator
     * @return new condition.
     */
    public Condition not(Operator aOperator)
    {
        return setNext(Function.NOT, aOperator);
    }

    /**
     * Prepares string builder representing chain of conditions for single
     * attribute. For example 'name = ? AND name LIKE ? OR name > ?'.
     * 
     * @param aAttributeName
     *            name of attribute, that condition is assigned to.
     * @param aIndex
     *            index of specific single condition.
     * @return chain of conditions for specified attribute's name.
     */
    public StringBuilder prepareString(String aAttributeName, int aIndex)
    {
        StringBuilder builder = new StringBuilder();
        if (aIndex > 0)
        {
            builder.append(" ").append(function.toString()).append(" ");
        }
        builder.append(" ").append(aAttributeName).append(" ")
                .append(operator.toString()).append(" ?");
        index = aIndex;
        int idx = aIndex + 1;
        if (next != null)
        {
            builder.append(" ").append(next.prepareString(aAttributeName, idx));
        }
        return builder;
    }

    /**
     * Calculates index of last condition in chain. To achieve proper behaviour
     * this method must be called after prepereString method.
     * 
     * @return index of last condition in this chain.
     */
    public int getIndex()
    {
        if (next != null)
        {
            return next.getIndex();
        }
        return index;
    }

    /**
     * Calculates indexes of all conditions in chain. First index equals index
     * from method's argument + 1.
     * 
     * @param aFirst
     *            first index
     * @return list of indexes.
     */
    public List<Integer> getParamsIndexes(int aFirst)
    {
        List<Integer> indexes = new ArrayList<>();
        indexes.add(aFirst);
        int idx = aFirst + 1;
        Condition nx = next;
        while (nx != null)
        {
            indexes.add(idx);
            nx = nx.getNext();
            idx++;
        }
        return indexes;
    }
}
