import React, { forwardRef, useState } from "react";
import axios from "axios";
import MaterialTable, { Icons} from "material-table";
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
import { Alert, AlertTitle } from "@mui/material";

function Groups() {
  interface Group {
    id: number;
    name: string;
    description: string | null;
  }

  const [groups, setGroups] = useState<Group[]>([]);
  const [iserror, setIserror] = useState<boolean>(false);
  const [errorMessages, setErrorMessages] = useState<string[]>([]);

  React.useEffect(() => {
    axios.get("/api/groups").then(
      (res) => {
        console.log(res.data);
        setGroups(res.data);
      },
      (err) => {
        console.log(err.data);
      }
    );
  }, []);

  const columns = [
    { field: "id", title: "ID", hidden: true },
    { field: "name", title: "Name" },
    { field: "description", title: "Description" },
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

  const handleRowUpdate = (
    newData: Group,
    oldData: any,
    resolve: any
  ): void => {
    let errorList = [];
    if (newData.name === "") {
      errorList.push("Try Again, You didn't enter the name field");
    }
    if (errorList.length < 1) {
      axios
        .post(`/api/group/${oldData.id}`, newData)
        .then((response) => {
          const updateUser = [...groups];
          const index = oldData.tableData.id;
          updateUser[index] = newData;
          setGroups([...updateUser]);
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
      .delete(`/api/group/${oldData.id}`)
      .then((response) => {
        const dataDelete = [...groups];
        const index = oldData.tableData.id;
        dataDelete.splice(index, 1);
        setGroups([...dataDelete]);
        resolve();
      })
      .catch((error) => {
        setErrorMessages(["Delete failed! Server error"]);
        setIserror(true);
        resolve();
      });
  };

  //function for adding a new row to the table
  const handleRowAdd = (newData: Group, resolve: any): void => {
    //validating the data inputs
    let errorList = [];
    if (newData.name === "") {
      errorList.push("Try Again, You didn't enter the name field");
    }
    if (errorList.length < 1) {
      axios
        .put(`/api/group`, newData)
        .then((response) => {
          let newGroup = [...groups];
          newGroup.push(response.data);
          setGroups(newGroup);
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
          data={groups}
          options={{
            pageSize: 10,
            actionsColumnIndex: -1,
          }}
          icons={tableIcons}
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

export default Groups;
