<?php require("config.php")?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" 
>
<html  xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    <link rel="stylesheet" href="images/styles.css" type="text/css" />
    <script type="text/javascript" src="scripts/script.js"></script>
    <title>Tox+: Main Page</title>
    <link href="http://opentox.org/favicon.ico" type="image/x-icon" rel="shortcut icon" />
</head>

<body>


<div id="container">

<?php include("header.php");?>
<?php include("nav.php");?>

<?php
$page = $_GET["p"];

$index = 0;
while ($pages[$index][0] != $page && $index<$pages_size) {
    $index++;
}

if ($index < $pages_size) { 
    include("pages/" . $pages[$index][1]);
} else {
    include("pages/home.php");
}
?>
<?php include("sidebar.php");?>
<?php include("footer.php");?>
</div>
</body>
</html>


