<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
>
<?php require("config.php"); ?>
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr">

<?php require("HEAD.php"); ?>

<body id="top">
<?php require("header.php"); ?>


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




<?php require("footer.php");?>

</body>
</html>
