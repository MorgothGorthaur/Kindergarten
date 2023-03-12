import {useState, useEffect} from "react";
 import {Button, Form} from "react-bootstrap";
 import Input from "../UI/Input/Input";
 import KidsService from "../API/KidsService";

 const ChildForm = ({child, kids, setKids, tokens, setTokens, setShowForm}) => {
 const [id, setId] = useState('');
 const [name, setName] = useState('');
 const [birthYear, setBirthYear] = useState('');
 useEffect(() => {
     if (child) {
         setId(child.id);
         setName(child.name);
         setBirthYear(child.birthYear);
     }
 }, [child]);

 const add = (e) => {
     e.preventDefault();
     KidsService.add(name, birthYear, tokens, setTokens).then(data => {
         console.log(data);
         if(data.debugMessage) alert(data.debugMessage)
         else {
             setKids([...kids, data]);
             setShowForm(false);
         }
     })
 }

 const update = (e) => {
     e.preventDefault();

     KidsService.update(id, name, birthYear, tokens, setTokens).then(data => {
         console.log(data);
         validation(data);
     })
 }

 const validation = (data) => {
     console.log(data)
     if(!data) {
         setKids([...kids.filter(k => k.id !== id), { id: id, name: name, birthYear: birthYear }])
         console.log(child);
         setShowForm(false);
     }
     else {
         alert(data.debugMessage);
     }
 }

 return (
     <Form className="form" onSubmit={child ? update : add}>
         <Input type="text" placeholder="name" value={name} onChange={e => setName(e.target.value)}/>
         <Input type="date" placeholder="birth year" value={birthYear} onChange={e => setBirthYear(e.target.value)}/>
         <Button type="submit"> {child ? "update" : "add"} </Button>
     </Form>
 )

 }
 export default ChildForm;