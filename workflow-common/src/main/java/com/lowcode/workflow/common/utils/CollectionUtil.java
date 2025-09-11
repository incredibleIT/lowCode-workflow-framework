package com.lowcode.workflow.common.utils;


import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;



public class CollectionUtil {


    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return true：为空 false：不为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null ||collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     * @param collection 集合
     * @return true：不为空 false：为空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 集合映射
     * @param collection 集合
     * @param function 映射函数
     * @return 映射后的集合
     * @param <T> 集合元素类型
     * @param <R> 映射后元素类型
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<? super T, ? extends R> function) {
        if (isEmpty(collection)) {
            // TODO 抛错
            return null;
        }
        return collection.stream().map(function).collect(Collectors.toList());
    }


    /**
     * 遍历集合做自定义操作
     * @param collection 集合
     * @param consumer 自定义操作
     * @param <T> 集合元素类型
     */
    public static <T> void foreach(Collection<T> collection, Consumer<? super T> consumer) {
        if (isNotEmpty(collection)) {
            collection.forEach(consumer);
        }
    }


    /**
     * 集合过滤
     * @param collection 集合
     * @param predicate 过滤条件
     * @return 过滤后的集合
     * @param <T> 集合元素类型
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isNotEmpty(collection)) {
            return collection.stream().filter(predicate).collect(Collectors.toList());
        }

        return null;
    }

    /**
     * 集合过滤并映射
     * @param collection 集合
     * @param predicate 过滤条件
     * @param function 映射函数
     * @return 映射后的集合
     * @param <T> 集合元素类型
     * @param <R> 映射后元素类型
     */
    public static <T, R> List<R> filterAndMap(Collection<T> collection, Predicate<T> predicate, Function<T, R> function) {
        if (isNotEmpty(collection)) {
            return collection.stream().filter(predicate).map(function).collect(Collectors.toList());
        }
        return null;
    }


    /**
     * 集合中是否存在不满足指定条件的元素
     * @param collection 集合
     * @param predicate 条件
     * @return true：存在 false：不存在
     * @param <T> 集合元素类型
     */
    public static <T> boolean noneMatch(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().noneMatch(predicate);
    }



}
