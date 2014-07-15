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
package pl.mpiglas.jqube.example;

import java.sql.Types;
import java.util.Arrays;

import pl.mpiglas.jqube.AttributesPair;
import pl.mpiglas.jqube.Condition;
import pl.mpiglas.jqube.Query;
import pl.mpiglas.jqube.QueryBuilder;
import pl.mpiglas.jqube.QueryView;
import pl.mpiglas.jqube.Schema;
import pl.mpiglas.jqube.TableView;
import pl.mpiglas.jqube.ViewsRelation;
import pl.mpiglas.jqube.Condition.Function;
import pl.mpiglas.jqube.Condition.Operator;
import pl.mpiglas.jqube.ViewAttribute.Aggregation;

/**
 * @author Milosz Piglas
 * 
 */
public class JqubeExample
{

    private static TableView booksView()
    {
        TableView v = new TableView("Books", "BOOKS");
        v.addAttribute("title", null, Types.VARCHAR);
        v.addAttribute("author", null, Types.VARCHAR);
        v.addAttribute("publisher", null, Types.INTEGER);
        v.addAttribute("year", null, Types.VARCHAR);
        v.addAttribute("category", null, Types.INTEGER);
        return v;
    }

    private static TableView publishersView()
    {
        TableView v = new TableView("Publishers", "PUBLISHERS");
        v.addAttribute("id", null, Types.INTEGER);
        v.addAttribute("name", null, Types.VARCHAR);
        v.addAttribute("city", null, Types.INTEGER);
        return v;
    }

    private static TableView enumView(String aViewName, String aSource,
            String aNameAtr)
    {
        TableView v = new TableView(aViewName, aSource);
        v.addAttribute("id", null, Types.INTEGER);
        v.addAttribute(aNameAtr, null, Types.VARCHAR);
        return v;
    }

    public static void main(String[] args)
    {
        
        // initialization of views
        TableView books = booksView();
        TableView publishers = publishersView();
        TableView cities = enumView("Cities", "CITIES", "city_name");
        TableView categories = enumView("Categories", "CATEGORIES",
                "category_name");

        // initialization of relations
        ViewsRelation booksPublishers = new ViewsRelation(
                Arrays.asList(new AttributesPair(books
                        .getAttribute("publisher"), publishers
                        .getAttribute("id"))));

        ViewsRelation booksCategories = new ViewsRelation(
                Arrays.asList(new AttributesPair(
                        books.getAttribute("category"), categories
                                .getAttribute("id"))));

        ViewsRelation publishersCities = new ViewsRelation(
                Arrays.asList(new AttributesPair(publishers
                        .getAttribute("city"), cities.getAttribute("id"))));

        
        // initialization of schema
        Schema schema = new Schema();
        schema.addView(books); // first view in schema
        schema.addView(publishers, booksPublishers);
        schema.addView(cities, publishersCities);
        schema.addView(categories, booksCategories);

        // preparing builder for nested query
        QueryBuilder subBuilder = new QueryBuilder(schema);
        subBuilder.add(books.getAttribute("author").select()
                .withAggregate(Aggregation.COUNT).withAliasName("Authors")
                .withOrderBy(true).build());
        subBuilder.add(categories.getAttribute("category_name").select()
                .withCondition(new Condition(Function.AND, Operator.LIKE))
                .withOrderBy(true).withGroupBy(true).build());
        subBuilder.add(books.getAttribute("publisher").select()
                .withGroupBy(true)
                .withCondition(new Condition(Function.AND, Operator.EQ))
                .build());
        subBuilder.add(publishers.getAttribute("name").select()
                .withGroupBy(true)
                .withAliasName("pub")
                .build());

        // multi condition for single attribute
        Condition cond = new Condition(Function.AND, Operator.EQ);
        cond.or(Operator.EQ).or(Operator.EQ);

        // new condition: year = ? OR year = ? OR year = ?
        subBuilder.add(books.getAttribute("year").select().withGroupBy(true)
                .withOrderBy(true).withCondition(cond).build());

        // building new view - it might be used as nested query
        QueryView subView = subBuilder.createView("AuthorsView");

        // relation between nested query and existing table's view
        ViewsRelation authorsPublishers = new ViewsRelation(
                Arrays.asList(new AttributesPair(subView
                        .getAttribute("publisher"), publishers
                        .getAttribute("id"))));
        
        schema.addView(subView, authorsPublishers);
        
        // main query - utilise relation between view 'publishers' and nested query ('Authors')
        QueryBuilder builder = new QueryBuilder(schema);
        builder.add(subView.getAttribute("Authors").select().withCondition(new Condition(Function.OR, Operator.EQ)).build());
        builder.add(publishers.getAttribute("name").select().build(), true);

        Query subQuery = subView.getQuery();
        Query mainQuery = builder.build();
        
        System.out.println("SUB "+subQuery.getStatement());
        System.out.println(subQuery.getSelectAttributes());
        System.out.println(subQuery.getParams());
        System.out.println("");
        System.out.println("MAIN "+mainQuery.getStatement());
        System.out.println(mainQuery.getSelectAttributes());
        System.out.println(mainQuery.getParams());
        System.out.println("");
    }

}
