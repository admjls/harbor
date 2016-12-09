# Harbor 

Harbor is a collection of content and page components, services, and patterns constituting a common starting point for projects.  From both an author and developer enablement perspective Harbor follows these tennants: 

* Composability
* Extensibility
* Cohesion
* David

## Components

### Harbor Title Component

* Group: Harbor

Presents the Page Title of the current page in an H1 DOM element.  The output H1 element will have an id attribute set to a sanitized version of the title text allowing for fragment linking.  Both the title text and element id may be overridden by authors.

#### Authorability

| Dialog Field | Description |
| ------------ | ----------- |
| Text         | Sets the rendered title text.  When left empty, the title text will default to the page's Page Title. |
| ID           | Sets the string ID to be attributed to the H1 element rendered for the title.  When left empty, the ID will be produced by sanitizing the title text. Indended to allow anchor linking to title elements. |

### Harbor Subtitle Component

* Group: Harbor

Presents the Page Subtitle of the current page in an H2 DOM element.  The output H2 element will have an id attribute set to a sanitized version of the subtitle text allowing for fragment linking.  Both the subtitle text and element id may be overridden by authors.

#### Authorability

| Dialog Field | Description |
| ------------ | ----------- |
| Text         | Sets the rendered subtitle text.  When left empty, the subtitle text will default to the page's Page Subtitle. |
| ID           | Sets the string ID to be attributed to the H1 element rendered for the subtitle.  When left empty, the ID will be produced by sanitizing the subtitle text. |

### Harbor Heading Component

* Group: Harbor

Presents a heading element appropriate for titling sections of content.  This component is NOT meant to be used as the principal title for content and as such only exposes heading options H2-H6.  If an H1 element is desired, the Title component should be used.

#### Authorability

| Dialog Field | Description |
| ------------ | ----------- |
| Size         | Establishes the size or level of the Heading.  Available sizes include H2 - H6.  If an H1 element is desired the Title component should be used instead. |
| Text         | Sets the rendered heading text. |
| ID           | Sets the string ID to be attributed to the rendered heading element.  When left empty, the ID will be produced by sanitizing the heading text. |

### Harbor Text Component

* Group: Harbor
* Classifiable

The Harbor Text component will allow the author to compose text within a Rich Text Editor configured to enable commonly used plugins.

#### Authorability

Text is edited via the text in-place editor.

| Dialog Field | Description |
| ------------ | ----------- |
| Classification | Input support for classifiability of the text component instance. |






## Core Concepts

### End User Core Concepts

_To be written_

### Developer Core Concepts

#### Lists

The functionality of many of the Components created for a particular project can be distilled down to the presentation of a List of items.  Lists therefore are central to Harbor and enabled by Interfaces and Abstract Classes suggesting a pattern to follow in List Component development.

List Components are a composition of a List Construction Strategy and a List Rendering Strategy.  The List Construction Strategy is responsible for the creation of a list of items based on the logic of the strategy.  These items are passed to the List Rendering Strategy which has an opportunity to add additional properties germane to the rendering of a particular component.  Construction and Rendering Strategies may be mixed and matched within the context of concrete List Component implementations allowing reuse of both across multiple component types.

TODO: Insert example of reuse from the various page listing components

TODO: Link to Javadocs where appropriate

#### Trees

Similar to Lists, Trees pair a Construction Strategy with a Rendering Strategy allowing for reusability and extention via composition.

A Tree has a single TreeNode instance as its root.  A TreeNode may in turn have any number of child TreeNodes of the same type.

Tree Components are in turn components which intend to manage a Tree data structure's construction and presentation.

TODO: Link to Javadocs where appropriate








#OLD

## Lists

Many of the components which are built for a particular project are, at their core, a list of things.  The concern
of such components then is three-fold.

1. Construct the list of things germane to the instance of the component
2. Transform the list of things found in step one into a list of things ready for rendering (in practice this step is often combined with step 1)
3. Produce a rendering or visualization of the transformed list of things

The List API proposed by Harbor encapsulates each step allowing for the development of new components based on a composition
of implementations of the three steps listed above.

### Step 1: List Construction and the ListConstructionStrategy

Implementations of the ListConstructionStrategy cover Step 1 of the list workflow laid-out above.

```java
public interface ListConstructionStrategy <T> {

    Iterable<T> construct();

}
```

Such an implementation exposes a single `construct` method which produces a generic `Iterable`.  An implementation would
generally encapsulate the logic necessary to build up the list of things of interest to the component based on any number
of component-specific mechanisms.  The generic type is intended to be a domain object unencumbered with properties specific
to its presentation.

### Step 2: List Transformation and the ListRenderingStrategy

Once a List of domain objects has been constructed by a ListConstructionStrategy, this list can be fed into a
ListRenderingStrategy.

```java
public interface ListRenderingStrategy <T, R extends Iterable<?>> {

    public R toRenderableList(Iterable<T> itemIterable);

}
```

Implementations of the ListRenderingStrategy expose a single `toRenderableList` method which takes as input the output
from a ListConstructionStrategy's `construct` method and produces an Iterable of some type.  In the generic signature,
implementations must provide the type produced by the ListConstructionStrategy and a type of Iterable to be returned.
As such, developers of implementations are free to define their own Iterable implementations which is useful if the
transformed list needs to contain information germane to the entire list and not just the individual elements (for example,
if the list's ability to render as an ordered or unordered list is authorable, this information is relevant to the entire
list and not the individual items).  The elements of the transformed list should expose methods proper to the rendering of the element.

### Step 3: List Visualization

Visualization of a list is left to the rendering mechanism employed, be it a .jsp, a servlet returning json, or any other
mechanic.  At a high level, rendering involves iterating over the Iterable produced by the `ListRenderingStrategy` and
rendering each item as necessary.

#### The RenderableItem Interface

Implementations of the `RenderableItem` interface represent list items which are able to produce their own rendering.

```java
public interface RenderableItem {

    public String render();

}
```

An implementation of `RenderableItem` exposes a single `render` method which is intended to return a String representation
of the item.  If appropriate, an implementation of this interface may be returned as the members of the Iterable produced
by the `ListRenderingStrategy`'s `toRenderableList` method after which rendering mechanisms may simply call the `render` method
on each item to produce a rendering.

#### The harbor:includeListItems JSP Tag

The `harbor:includeListItems` tag is provided as a rendering convenience for those components using .jsp as their
rendering mechanism.

Attribute Name | Attribute Type | Required | Description
-------------- | -------------- | -------- | -----------
items          | Iterable<?>    | true     | The Iterable of items generally produced by a `ListRenderingStrategy`
itemVar        | String         | false    | If using a script for rendering, this is the variable name by which the current item may be referred to
script         | String         | false    | A relative reference to a .jsp script which should be used to render each item.  This is required if your items do not implement `RenderableItem`.

The tag will iterate over all items in the Iterable provided rendering each in turn.  If a `script` attribute is specified,
the tag will use the specified .jsp script to render the individual items.  Within the context of said script the item's name
is the name provided in the `itemVar` attribute of the `harbor:includeListItems` tag.  If your list items themselves implement
the `RenderableItem` interface then you need not include a `script` or `itemVar` attribute, the tag will simply use the
`render` method exposed by `RenderableItem` to produce a rendering of the item.

### List APIs and the Component Plugin

Using these APIs, new List components are made of a composition of a `ListConstructionStrategy` and a `ListRenderingStrategy`.  This
composition approach also allows for the creation of an authoring dialog via composition.  By attributing a `@DialogFieldSet` annotation
to both the `ListConstructionStrategy` and the `ListRenderingStrategy` and further annotating the members of these strategies with
appropriate `@DialogField` annotations, both your component and your authoring can be composed.  The
`com.citytechinc.aem.harbor.core.components.content.navigation.bootstrapnavigation.mainnavigation.BootstrapMainAutoNavigation`
component is a reasonable exemplar of this type of implementation.

### The ListComponent and AbstractListComponent

The `ListComponent` Interface and `AbstractListComponent` abstract class establish a general pattern for list component
composition.

```java
public interface ListComponent <T extends Iterable> {

    public T getItems();

    public Iterator<?> getIterator();

}
```

A `ListComponent` is known to produce some manner of Iterable.  This mirrors what the `ListRenderingStrategy` is able
to produce.  As a convenience, a `ListComponent` implementation will expose a `getIterator` method which is useful if
rendering using .jsp since the jstl `forEach` tag can iterate over an Iterator but not over an Iterable.

The 'AbstractListComponent` abstract class represents the super class of a standard List component.  Among other things it
exposes two abstract methods.

```java
protected abstract ListConstructionStrategy<T> getListConstructionStrategy();

protected abstract ListRenderingStrategy<T, R> getListRenderingStrategy();
```

These methods respecively return the `ListConstructionStrategy` and `ListRenderingStrategy` which the component employ.
The `AbstractListComponent` implements the `ListComponent` interface.  The object returned by the `AbstractListComponent`s
`getItems` method is the result of calling the rendering strategy's `toRenderableList` method on the results of the construction
 strategy's `construct` method.

*More Documentation Coming*