/**
Random element from any fixed size array
**/
public <E> E random(E[] arr){
  return arr[floor(random(arr.length))];
}

/**
Random element from any arraylist
**/
public <E> E random(ArrayList<E> list){
  return (list.get(floor(random(list.size()))));
}

/** 
Returns if the random small change was met. For example oneIn(50) has a 
2 percent change of succeeding
*/
boolean oneIn(int chance){
  return random(1) < (1 / chance);
}