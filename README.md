# HomeScreenDemo

Possible solution for scrolling in HomeScreen. (Need to create custom LayoutManager for animated or smooth scroll possibility)

Using RecyclerView as base View with composite adapter.

Composit adapter combine all given types of RecyclerView adapters in 1 adapter (posibility scroll up-down).

ViewHolders of composit adapter is RecyclerView with given type of adapter (delegates).

ViewHolders of delegates are simple ItemsView wich can be scroll left-right.

Main idea = RecyclerViews (HORIZONTAL SCROLL) as ViewHolders in BaseRecyclerView (VERTICAL SCROLL)

AWARE: Tested only on mobile platform (don't contain logic of selection (ViewHolders of BaseRecyclerView)). 

Can contains focus issue for STB. As suggestion for scrolling up-down need request focus on BaseRecyclerView, for scrolling left-right need request focus on ViewHolder of BaseRecyclerView.

![alt tag](http://i.imgur.com/u1ZZUWQ.gif)
