# HomeScreenDemo

Possible solution for scrolling in HomeScreen.
Using RecyclerView as base View with composite adapter.
Composit adapter combine all given types of RecyclerView adapters in 1 adapter (posibility scroll up-down).
ViewHolder of composit adapter is RecyclerView with given type of adapter (delegates).
ViewHolder of delegates are simple ViewItems witch can be scroll left-right.

Main idea = RecyclerViews (HORIZONTAL SCROLL) as ViewHolders in RecyclerView (VERTICAL SCROLL)

![alt tag](http://i.imgur.com/u1ZZUWQ.gif)
