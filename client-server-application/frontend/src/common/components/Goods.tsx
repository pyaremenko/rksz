import React, { forwardRef, useState } from "react";
import axios from "axios";
import MaterialTable, { Column, Icons, MTableBody } from "material-table";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import CheckIcon from "@mui/icons-material/Check";
import ArrowDownwardIcon from "@mui/icons-material/ArrowDownward";
import ClearIcon from "@mui/icons-material/Clear";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import SaveAltIcon from "@mui/icons-material/SaveAlt";
import FilterListIcon from "@mui/icons-material/FilterList";
import FirstPageIcon from "@mui/icons-material/FirstPage";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import LastPageIcon from "@mui/icons-material/LastPage";
import RemoveIcon from "@mui/icons-material/Remove";
import ViewColumnIcon from "@mui/icons-material/ViewColumn";
import SearchIcon from "@mui/icons-material/Search";
import {
  Alert,
  AlertTitle,
  TableCell,
  TableRow,
  Typography,
} from "@mui/material";

function Goods() {
  interface Good {
    id: number;
    name: string;
    groupId: string;
    description: string | null;
    manufacturer: string | null;
    number: number;
    price: number;
    totalPrice: string;
  }

  interface Group {
    id: string;
    name: string;
  }

  const [groups, setGroups] = useState<Group[]>([]);
  const [goods, setGoods] = useState<Good[]>([]);
  const [iserror, setIserror] = useState<boolean>(false);
  const [errorMessages, setErrorMessages] = useState<string[]>([]);

  React.useEffect(() => {
    axios.get("/api/goods").then(
        (res) => {
          let resultGoods = res.data.map((value: any) => {
            let tableRowValue: Good = {
              id: value.id,
              name: value.name,
              groupId: value.group.id.toString(),
              description: value.description,
              manufacturer: value.manufacturer,
              number: value.number,
              price: value.price,
              totalPrice: (value.number * value.price).toFixed(2),
            };
            return tableRowValue;
          });
          setGoods(resultGoods);
        },
        (err) => {}
    );
    axios.get("/api/groups").then(
        (res) => {
          setGroups(res.data);
        },
        (err) => {}
    );
  }, []);

  var groupsLookup = groups.reduce(function (acc: any, cur, i) {
    acc[cur.id] = cur.name;
    return acc;
  }, {});

  const columns: Column<Good>[] = [
    { field: "id", title: "ID", hidden: true },
    { field: "name", title: "Name", filtering: false },
    { field: "groupId", title: "Group", filtering: true, lookup: groupsLookup },
    { field: "description", title: "Description", filtering: false },
    { field: "manufacturer", title: "Manufacturer", filtering: false },
    {
      field: "number",
      title: "Number",
      type: "numeric" as const,
      filtering: false,
    },
    {
      field: "price",
      title: "Price",
      type: "currency" as const,
      filtering: false,
    },
    {
      field: "totalPrice",
      title: "Total price",
      type: "currency" as const,
      filtering: false,
      editable: "never" as const,
    },
  ];

  const tableIcons: Icons = {
    Add: forwardRef((props, ref) => <AddIcon {...props} ref={ref} />),
    Check: forwardRef((props, ref) => <CheckIcon {...props} ref={ref} />),
    Clear: forwardRef((props, ref) => <ClearIcon {...props} ref={ref} />),
    Delete: forwardRef((props, ref) => (
        <DeleteOutlineIcon {...props} ref={ref} />
    )),
    DetailPanel: forwardRef((props, ref) => (
        <ChevronRightIcon {...props} ref={ref} />
    )),
    Edit: forwardRef((props, ref) => <EditIcon {...props} ref={ref} />),
    NextPage: forwardRef((props, ref) => (
        <ChevronRightIcon {...props} ref={ref} />
    )),
    ResetSearch: forwardRef((props, ref) => <ClearIcon {...props} ref={ref} />),
    SortArrow: forwardRef((props, ref) => (
        <ArrowDownwardIcon {...props} ref={ref} />
    )),
    Export: forwardRef((props, ref) => <SaveAltIcon {...props} ref={ref} />),
    Filter: forwardRef((props, ref) => <FilterListIcon {...props} ref={ref} />),
    FirstPage: forwardRef((props, ref) => (
        <FirstPageIcon {...props} ref={ref} />
    )),
    PreviousPage: forwardRef((props, ref) => (
        <ChevronLeftIcon {...props} ref={ref} />
    )),
    Search: forwardRef((props, ref) => <SearchIcon {...props} ref={ref} />),
    LastPage: forwardRef((props, ref) => <LastPageIcon {...props} ref={ref} />),
    ThirdStateCheck: forwardRef((props, ref) => (
        <RemoveIcon {...props} ref={ref} />
    )),
    ViewColumn: forwardRef((props, ref) => (
        <ViewColumnIcon {...props} ref={ref} />
    )),
  };

  const handleRowUpdate = (newData: Good, oldData: any, resolve: any): void => {
    let errorList = [];
    if (!newData.name) {
      errorList.push("Try Again, You didn't enter the name field");
    }
    if (!newData.price) {
      errorList.push("Try Again, You didn't enter the price field");
    }
    if (!newData.number) {
      errorList.push("Try Again, You didn't enter the number field");
    }
    if (!newData.groupId) {
      errorList.push("Try Again, You didn't enter the group field");
    }
    if (errorList.length < 1) {
      axios
          .post(`/api/good/${oldData.id}`, newData)
          .then((response) => {
            const updateUser = [...goods];
            const index = oldData.tableData.id;
            updateUser[index] = {
              ...newData,
              totalPrice: (newData.number * newData.price).toFixed(2),
            };
            setGoods([...updateUser]);
            resolve();
            setIserror(false);
            setErrorMessages([]);
          })
          .catch((error) => {
            setErrorMessages(["Update failed! Server error"]);
            setIserror(true);
            resolve();
          });
    } else {
      setErrorMessages(errorList);
      setIserror(true);
      resolve();
    }
  };

  //function for deleting a row
  const handleRowDelete = (oldData: any, resolve: any): void => {
    axios
        .delete(`/api/good/${oldData.id}`)
        .then((response) => {
          const dataDelete = [...goods];
          const index = oldData.tableData.id;
          dataDelete.splice(index, 1);
          setGoods([...dataDelete]);
          resolve();
        })
        .catch((error) => {
          setErrorMessages(["Delete failed! Server error"]);
          setIserror(true);
          resolve();
        });
  };

  //function for adding a new row to the table
  const handleRowAdd = (newData: Good, resolve: any): void => {
    //validating the data inputs
    let errorList = [];
    if (!newData.name) {
      errorList.push("Try Again, You didn't enter the name field");
    }
    if (!newData.price) {
      errorList.push("Try Again, You didn't enter the price field");
    }
    if (!newData.number) {
      errorList.push("Try Again, You didn't enter the number field");
    }
    if (!newData.groupId) {
      errorList.push("Try Again, You didn't enter the group field");
    }
    if (errorList.length < 1) {
      axios
          .put(`/api/good`, newData)
          .then((response) => {
            let newGroup = [...goods];
            newGroup.push({
              ...newData,
              id: response.data.id,
              totalPrice: (newData.number * newData.price).toFixed(2),
            });
            setGoods(newGroup);
            resolve();
            setErrorMessages([]);
            setIserror(false);
          })
          .catch((error) => {
            setErrorMessages(["Cannot add data. Server error!"]);
            setIserror(true);
            resolve();
          });
    } else {
      setErrorMessages(errorList);
      setIserror(true);
      resolve();
    }
  };

  let handleGoodCountChange = (good: any, number: number) => {
    axios
        .post(`/api/good/number/${good.id}`, null, {
          params: {
            number,
          },
        })
        .then((response) => {
          const updateGood = [...goods];
          const index = good.tableData.id;
          updateGood[index] = {
            ...good,
            number: response.data,
            totalPrice: (good.number * good.price).toFixed(2),
          };
          setGoods([...updateGood]);
          setIserror(false);
          setErrorMessages([]);
        })
        .catch((error) => {
          setErrorMessages(["Update failed! Server error"]);
          setIserror(true);
        });
  };

  let handleMinus = (event: any, rowData: any) => {
    let number = parseInt(prompt("Enter number")!);
    if (isNaN(number) || number <= 0 || number > rowData.number) {
      alert("Invalid number");
    } else {
      handleGoodCountChange(rowData, number);
    }
  };

  let handlePlus = (event: any, rowData: any) => {
    let number = parseInt(prompt("Enter number")!);
    if (isNaN(number) || number <= 0) {
      alert("Invalid number");
    } else {
      handleGoodCountChange(rowData, -number);
    }
  };

  return (
      <>
        <div>
          <div>
            {iserror && (
                <Alert severity="error">
                  <AlertTitle>ERROR</AlertTitle>
                  {errorMessages.map((msg, i) => {
                    return <div key={i}>{msg}</div>;
                  })}
                </Alert>
            )}
          </div>
          <MaterialTable
              title={""}
              columns={columns}
              data={goods}
              options={{
                pageSize: 10,
                thirdSortClick: false,
                actionsColumnIndex: -1,
                filtering: true,
              }}
              icons={tableIcons}
              actions={[
                {
                  icon: () => <AddIcon />,
                  tooltip: "Save User",
                  onClick: handlePlus,
                },
                {
                  icon: () => <RemoveIcon />,
                  tooltip: "Save User",
                  onClick: handleMinus,
                },
              ]}
              components={{
                Body: (props) => {
                  let sum = 0;
                  props.renderData.forEach((element: Good) => {
                    sum += +element.totalPrice;
                  });
                  return (
                      <>
                        <TableRow>
                          <TableCell colSpan={7} align="right">
                            <Typography fontWeight={"bold"}>
                              Total price: ${sum}
                            </Typography>
                          </TableCell>
                          <TableCell colSpan={1}></TableCell>
                        </TableRow>
                        <MTableBody {...props} />
                      </>
                  );
                },
              }}
              editable={{
                onRowUpdate: (newData, oldData) =>
                    new Promise<void>((resolve) => {
                      handleRowUpdate(newData, oldData, resolve);
                    }),
                onRowAdd: (newData) =>
                    new Promise<void>((resolve) => {
                      handleRowAdd(newData, resolve);
                    }),
                onRowDelete: (oldData) =>
                    new Promise<void>((resolve) => {
                      handleRowDelete(oldData, resolve);
                    }),
              }}
          />
        </div>
      </>
  );
}

export default Goods;
