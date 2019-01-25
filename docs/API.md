# API document

## Suggestion Module

This module is for making suggestion when user is typing a keyword.

### URL

/MovieServer/Suggestion

### Reqeust

| Parameter | Description |
|---|---|
| keyword | The keyword that user is typing |
| count | Number of results on each search |

Example

```
?keyword=aaa&count=10
```

### Response

| Parameter | Description |
| title | Category's title (Constant: Movies, Stars) |
| children | Data source array of those 2 categories |
| title | Title of movie / star |
| id | ID of movie / star |

Example

```json
{
  "type": "success",
  "message": {
    "suggestions": [{
      "title": "Movies",
      "children": [
        {
          "title": "Movie 1",
          "id": 1
        },
        {
          "title": "Movie 2",
          "id": 2
        },
        {
          "title": "Movie 3",
          "id": 3
        }
      ]
    },
    {
      "title": "Stars",
      "children": [
        {
          "title": "Star 1",
          "id": 1
        },
        {
          "title": "Star 2",
          "id": 2
        },
        {
          "title": "Star 3",
          "id": 3
        }
      ]
    }
  ]
}
```

## Auto Complete Search Module

This module is for searching with only 1 specific keyword

### URL

/MovieServer/AutoComplete

### Reqeust

| Parameter | Description |
|---|---|
| keyword | keyword |

Example

```
?keyword=aaa
```

### Response

| Parameter | Description |
| results | Results of each search |

Example

```json
{
  "type": "success",
  "message": {
    "results": {
      // movie results
      "movies": [
        {
          "stars": [
            {
              "id": 1,
              "name": "Joji Matsuoka",
            },
            {
              "id": 2,
              "name": "Yui Natsukawa",
            }
          ],
          "year": "2001",
          "director": "Joji Matsuoka",
          "genres": ["Family", "Drama"],
          "id": "tt0278905",
          "title": "Acacia Walk"
        },
        {
          "stars": [
            {
              "id": 1,
              "name": "Joji Matsuoka",
            },
            {
              "id": 2,
              "name": "Yui Natsukawa",
            }
          ],
          "year": "2005",
          "director": "Sathyan Anthikad",
          "genres": ["Drama", "Family", "Comedy"],
          "id": "tt0446266",
          "title": "Achuvinte Amma"
        }
      ]
    },
    // star results
    "stars": [
      {
        "id": 1,
        "name": "Jack"
      },
      {
        "id": 2,
        "name": "Amy"
      }
    ]
  }
}
```