GET /polls - returns all polls \
Result:
```json5
[
        {
                "id": 1,
                "title": "Test",
                "description": "Test",
                "start_at": "2022-01-01T00:00:00",
                "end_at": "2022-01-01T00:00:01",
                "questions": {
                        "42a86ac14ae34db9bc6b739b205d0a98": {
                                "text": "Test",
                                "type": "SINGLE"
                        }, //...
                }
        }
]
```

GET /polls/{pollId} - returns poll with the given id \
Call example:
```json5
{
        "id": {pollId},
        "title": "Test",
        "description": "Test",
        "start_at": "2022-01-01T00:00:00",
        "end_at": "2022-01-01T00:00:01",
        "questions": {
                "42a86ac14ae34db9bc6b739b205d0a98": {
                        "text": "Test",
                        "type": "SINGLE"
                }
        }, //...
}
```

POST /polls/new - creates new post from request body \
Request body:
```json5
{
    "title": "Test",
    "description": "Test",
    "start_at": "2022-01-01T00:00:00",
    "end_at": "2022-01-01T00:00:01",
    "questions" : [
        {
            "text": "Test",
            "type": "SINGLE",
            "answers": [
                "Yes",
                "No" 
            ]
        }, //...
    ]
}
```
Call example:
```json5
{
        "id": 1,
        "title": "Test",
        "description": "Test",
   		"start_at": "2022-01-01T00:00:00",
    	"end_at": "2022-01-01T00:00:01",	
        "questions": {
                "5b856ce956f14581854eaf3f7da873f3": {
            		"text": "Test",
            		"type": "SINGLE",
            		"answers": [
                		"Yes",
               			 "No" 
            		]
        }, //...
        }
}
```

PUT /polls/{pollId}/update - updates poll with the given id \
Request Body:
```json5
{
    "title": "Test",
    "description": "Test",
    "end_at": "2022-01-01T00:00:01"
}
```
Result:
```json5
{
        "id": {pollId},
        "title": "Test",
        "start_at": "2022-01-01T00:00:00",
        "end_at": "2022-01-01T00:00:01",
        "questions": {
                "42a86ac14ae34db9bc6b739b205d0a98": {
                        "text": "Test",
                        "type": "TEXT"
                },//...
        }
}
```
DELETE /polls/{pollId}/delete - removes poll with the given id \
Result: number of removed rows
