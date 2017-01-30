name: title
# Databricks Platform

### Manuel Walser, January 2017

.footnote[Slides:  https://higgser.github.io/databricks/  
Code: https://github.com/higgser/databricks]

???
* Compile with: `markdown-to-slides -s Remark.css Databricks.md -o index.html`
* Created with [remark.js](https://github.com/gnab/remark) and [Markdown to slides](https://github.com/partageit/markdown-to-slides)
* Experience from Spark Summit Europe 2016 (Twitter Workshop)
* Press ? or H to see keyboard shortcuts
* **Login and Start Cluster now**

---
name:overview
## Databricks Overview
* Managed Platform for running Apache Spark
  https://databricks.com/product/databricks
* Making big data analytics easy (and accessible for everyone)
  https://databricks.com/try-databricks
* Pricing and Editions
  https://databricks.com/product/pricing
* Built-in Documentation and Examples

![Databricks Stack](databricks-stack-diagram.png)

???
* Simplifies cluster management and maintaince tasks
* Point&Click Platform for DS and DA
* REST API to automate data workflows
* Role-based access control and other intelligent optimizations
* **Open Pricing and Feature Page**
  *  AWS pricing starts at 0.33 USD/h On-Demand for 'r3.xlarge'

---
name: demo
## Features and Demo
.pull-left[
Community
* Mini-Cluster
* Notebooks
* Documentation
* Dashboards
* Libraries
* 3 Users

Professional
* Jobs Mgmt
* Access Control
* REST API
* IDE Integration
* GitHub
* Single Sign-On
]

.pull-right[![Databricks Stack](databricks-workspace.png)]

???
* Workspace: File-Explorer
* Notebooks: Files with Cells to execute commands
* Dashboards: Styled and filtered notebook output (Cluster must be running)
* Libraries: Files with additional functionality
* Tables: Database Tables (Cluster must be running)
* Cluster: Group of nodes, Spark Web UI, Logs, Libraries  
* Jobs: Schedule Notebooks/Jars, Notebook Workflows
* Apps: Third Party Integration, e.g. Tableau
* Menu-Bar, Search-Button

---
name: notebookFeatures
## Notebook Features
.table.table-condensed.table-striped.table-hover[
| Databricks | Zeppelin | Jupiter
--- | --- | --- | ---
Notebook | .label.label-success[Yes] | .label.label-success[Yes] | .label.label-success[Yes]
Autocompletion | .label.label-warning[Basic] | .label.label-warning[Basic] | .label.label-warning[Basic]
Languages | .label.label-success[Spark] | .label.label-success[Polyglot] | .label.label-success[Polyglot]
Playground | .label.label-success[Online] | .label.label-warning[Docker] | .label.label-success[No Spark]
Desktop Mode | .label.label-danger[No] | .label.label-success[Yes] | .label.label-success[Yes]
Data Visualizations | .label.label-success[Nice] | .label.label-warning[Basic] | .label.label-danger[No]
Git support | .label.label-success[Prof.] | .label.label-warning[File] | .label.label-warning[File]
Dashboards | .label.label-success[Yes] | .label.label-warning[iFrame] | .label.label-warning[plotly]
]

.footnote[Languages: Scala, Python, R, (Spark)SQL, Markdown, Shell  
Alternatives: [Beaker](http://beakernotebook.com/), [Spark-Notebook](http://spark-notebook.io)
]

???
* Create new Databricks and AWS Account (same AWS region, takes about 60 minutes)
* Databricks Account is linked to Amazon Account, You are responsible for the AWS costs of clusters you create (Deploy to AWS using Cross Account Role or Access Key)
* Open Databricks example notebook Spark for beginners
* Create a notebook, Output Markdown, SparkSession `spark.version`
* Start a Cluster, Show Link to Spark UI
* Import a library named bahir
* Import Twitter streaming notebook from Git https://github.com/higgser/databricks/blob/master/Twitter/twitter-streaming.scala
* Run the whole notebook
* Show Execution Graph for last plot
* Modify plot and switch to table
* Create Dashboard, Add Graphs
  https://dbc-a0e28f3c-9620.cloud.databricks.com/#notebook/258/dashboard/280/present
* Open dashboard notebook
* Show Tables with teets and votes
* Show Revision History, Git Integration, and Export Options
* Show User Management and Permissions
* Show Documentation and search fields

---
name: cloudFeatures
## Cloud Features
.table.table-condensed.table-striped.table-hover[
| Databricks | AWS/Zeppelin | AWS/JupiterHub
--- | --- | ---
Cluster Management | .label.label-success[Yes] | .label.label-warning[AWS] | .label.label-warning[AWS]
Schedule Jobs | .label.label-success[Yes] | .label.label-danger[No] | .label.label-danger[No]
Collaboration | .label.label-success[Yes] | .label.label-warning[Git] | .label.label-warning[Git]
Security/Permissions | .label.label-success[Easy] | .label.label-danger[No] | .label.label-warning[Basic]
REST API | .label.label-success[Yes] | .label.label-warning[Livy] | .label.label-warning[Basic]
]

---
name: links
## Links
Databricks
- [Community Login](https://community.cloud.databricks.com), [Pricing](https://databricks.com/product/pricing)
- [Documentation](https://docs.databricks.com/), [REST API](https://docs.cloud.databricks.com/docs/latest/databricks_guide/01%20Databricks%20Overview/10%20REST%20API.html), [TensorFlow](https://docs.databricks.com/applications/deep-learning/tensorflow.html)

Other
- [AWS Pricing](https://aws.amazon.com/de/ec2/pricing/on-demand)
- [Jupyter Playground](https://try.jupyter.org/)

Videos
- [Spark Summit SF 2016](https://spark-summit.org/2016/schedule/)
- [Spark Summit Europe 2016](https://spark-summit.org/eu-2016/schedule)

---
name: questions
## Discussion, Q&A

???
- How much disk space?
- When is data (e.g. /tmp/) deleted?

---
## Missing Features/Drawbacks/Bugs/...
- Search and replace (requested), Code refactoring
- Select, move, delete multiple cells
- Auto-refresh plots with streaming data
- Update Dashboard on widget change
- Deploy trained mode as WebService
- Save/Export Dashboards

---
### Howto run Zeppelin with Docker
Ubuntu Docker image (Ubuntu, Zeppelin 0.6.2, Spark 2.1.0, Scala 2.11)
```bash
docker run -d -p 8080:8080 -p 7077:7077 -p 4040:4040 epahomov/docker-zeppelin
```

### Howto run Jupiter
After installation with `pip3 install jupyter`
```bash
jupyter notebook
```
