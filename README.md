# Speech Analyzer

This project processes data from CSV files. It provides an endpoint that can handle one or multiple URLs for processing. Example CSV file exists into example folder.

## Usage

Here's a cURL example demonstrating how to process a two URL:

```
curl --location --request POST 'localhost:8080/evaluation?url=%3Cfull_path_csv_file_1%3E&url=%3Cfull_path_csv_file_2%3E'
```
